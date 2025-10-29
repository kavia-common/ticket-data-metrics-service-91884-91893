package com.example.devxdashboardbackend.service;

import com.example.devxdashboardbackend.exception.BadRequestException;
import com.example.devxdashboardbackend.exception.UnprocessableEntityException;
import com.example.devxdashboardbackend.model.TicketMetric;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * PUBLIC_INTERFACE
 * Service that parses uploaded Excel files and computes ticket metrics.
 * Parsing assumptions:
 *  - First row contains headers. We try to align to known columns if present.
 *  - We compute one TicketMetric per application-month aggregate as placeholder logic
 *    due to lack of detailed SRS computation rules in code scope.
 */
@Service
public class TicketMetricsService {

    private static final long MAX_SIZE_BYTES = 5L * 1024 * 1024;
    private static final String XLSX_MIME = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private static final Set<String> REQUIRED_COLUMNS = Set.of(
            "application", "month"
    );

    // Accepted columns that may inform computations if present
    private static final Set<String> OPTIONAL_COLUMNS = Set.of(
            "tickets_received", "tickets_responded", "respond_mttr_min", "response_adherence",
            "tickets_resolved", "resolve_mttr_min", "resolution_adherence", "remarks", "resolution_remarks"
    );

    /**
     * PUBLIC_INTERFACE
     * Validates and parses the uploaded Excel file into a list of TicketMetric objects.
     * @param file Multipart file (.xlsx) under field 'file'
     * @return list of computed TicketMetric
     */
    public List<TicketMetric> processUpload(MultipartFile file) {
        validateFile(file);

        try (InputStream in = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(in)) {

            Sheet sheet = workbook.getNumberOfSheets() > 0 ? workbook.getSheetAt(0) : null;
            if (sheet == null) {
                throw new UnprocessableEntityException("No sheet found in Excel file.");
            }

            Iterator<Row> rowIterator = sheet.rowIterator();
            if (!rowIterator.hasNext()) {
                throw new UnprocessableEntityException("Excel file is empty.");
            }

            // Read header row
            Row headerRow = rowIterator.next();
            Map<String, Integer> colIndex = readHeader(headerRow);

            // Validate required columns
            List<String> missing = new ArrayList<>();
            for (String req : REQUIRED_COLUMNS) {
                if (!colIndex.containsKey(req)) missing.add(req);
            }
            if (!missing.isEmpty()) {
                throw new UnprocessableEntityException("Missing required columns: " + String.join(", ", missing));
            }

            // Aggregate per (application, month)
            Map<String, TicketMetric> aggregates = new LinkedHashMap<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row == null) continue;

                String application = getStringCell(row, colIndex.get("application"));
                String month = getStringCell(row, colIndex.get("month"));

                if (isBlank(application) && isBlank(month)) {
                    continue; // skip empty lines
                }
                if (isBlank(application) || isBlank(month)) {
                    // row-level validation could be stricter; for now skip malformed lines
                    continue;
                }

                String key = application.trim() + "||" + month.trim();
                TicketMetric metric = aggregates.computeIfAbsent(key, k -> {
                    TicketMetric tm = new TicketMetric();
                    tm.setApplication(application.trim());
                    tm.setMonth(month.trim());
                    tm.setNoOfTicketsReceived(0);
                    tm.setNoOfTicketsRespondedByTel(0);
                    tm.setMttrRespondMin(0.0);
                    tm.setAdherenceToResponseSLA(0);
                    tm.setSlippedResponseSLA(0);
                    tm.setResponseAdherenceRate(0.0);
                    tm.setNoOfTicketsResolvedByTel(0);
                    tm.setMttrResolveMin(0.0);
                    tm.setAdherenceToResolutionSLA(0);
                    tm.setSlippedResolutionSLA(0);
                    tm.setResolutionAdherenceRate(0.0);
                    tm.setRemarks(null);
                    tm.setResolutionRemarks(null);
                    return tm;
                });

                // Placeholder computations:
                // If optional columns exist, we use their values; otherwise we increment counts as naive fallback.
                if (colIndex.containsKey("tickets_received")) {
                    Integer v = getIntegerCell(row, colIndex.get("tickets_received"));
                    if (v != null) metric.setNoOfTicketsReceived(safeAdd(metric.getNoOfTicketsReceived(), v));
                } else {
                    metric.setNoOfTicketsReceived(safeAdd(metric.getNoOfTicketsReceived(), 1));
                }

                if (colIndex.containsKey("tickets_responded")) {
                    Integer v = getIntegerCell(row, colIndex.get("tickets_responded"));
                    if (v != null) metric.setNoOfTicketsRespondedByTel(safeAdd(metric.getNoOfTicketsRespondedByTel(), v));
                }

                if (colIndex.containsKey("respond_mttr_min")) {
                    Double v = getDoubleCell(row, colIndex.get("respond_mttr_min"));
                    if (v != null) metric.setMttrRespondMin(safeAdd(metric.getMttrRespondMin(), v));
                }

                if (colIndex.containsKey("response_adherence")) {
                    // interpret as percentage adherence integer or count met
                    Integer v = getIntegerCell(row, colIndex.get("response_adherence"));
                    if (v != null) metric.setAdherenceToResponseSLA(safeAdd(metric.getAdherenceToResponseSLA(), v));
                }

                if (colIndex.containsKey("tickets_resolved")) {
                    Integer v = getIntegerCell(row, colIndex.get("tickets_resolved"));
                    if (v != null) metric.setNoOfTicketsResolvedByTel(safeAdd(metric.getNoOfTicketsResolvedByTel(), v));
                }

                if (colIndex.containsKey("resolve_mttr_min")) {
                    Double v = getDoubleCell(row, colIndex.get("resolve_mttr_min"));
                    if (v != null) metric.setMttrResolveMin(safeAdd(metric.getMttrResolveMin(), v));
                }

                if (colIndex.containsKey("resolution_adherence")) {
                    Integer v = getIntegerCell(row, colIndex.get("resolution_adherence"));
                    if (v != null) metric.setAdherenceToResolutionSLA(safeAdd(metric.getAdherenceToResolutionSLA(), v));
                }

                if (colIndex.containsKey("remarks")) {
                    String v = getStringCell(row, colIndex.get("remarks"));
                    if (!isBlank(v)) metric.setRemarks(v);
                }

                if (colIndex.containsKey("resolution_remarks")) {
                    String v = getStringCell(row, colIndex.get("resolution_remarks"));
                    if (!isBlank(v)) metric.setResolutionRemarks(v);
                }
            }

            // Post-process averages and rates where possible
            for (TicketMetric tm : aggregates.values()) {
                // Compute response adherence rate if both adherence and total responded known
                if (tm.getNoOfTicketsRespondedByTel() != null && tm.getNoOfTicketsRespondedByTel() > 0) {
                    if (tm.getAdherenceToResponseSLA() != null) {
                        tm.setResponseAdherenceRate(
                                clampRate(100.0 * tm.getAdherenceToResponseSLA() / tm.getNoOfTicketsRespondedByTel())
                        );
                        tm.setSlippedResponseSLA(Math.max(0, tm.getNoOfTicketsRespondedByTel() - tm.getAdherenceToResponseSLA()));
                    }
                }

                if (tm.getNoOfTicketsResolvedByTel() != null && tm.getNoOfTicketsResolvedByTel() > 0) {
                    if (tm.getAdherenceToResolutionSLA() != null) {
                        tm.setResolutionAdherenceRate(
                                clampRate(100.0 * tm.getAdherenceToResolutionSLA() / tm.getNoOfTicketsResolvedByTel())
                        );
                        tm.setSlippedResolutionSLA(Math.max(0, tm.getNoOfTicketsResolvedByTel() - tm.getAdherenceToResolutionSLA()));
                    }
                }
            }

            return new ArrayList<>(aggregates.values());
        } catch (UnprocessableEntityException e) {
            throw e;
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            // If it's a POI/format issue, map to 422
            throw new UnprocessableEntityException("Failed to parse Excel content: " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("No file uploaded under form field 'file'.");
        }
        if (file.getSize() > MAX_SIZE_BYTES) {
            // MaxUploadSizeExceededException may be thrown by Spring as well; we still guard here.
            throw new BadRequestException("File size exceeds the 5 MB limit.");
        }

        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase(Locale.ROOT) : "";
        if (!originalFilename.endsWith(".xlsx")) {
            throw new BadRequestException("Invalid file type. Only .xlsx files are supported.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !XLSX_MIME.equalsIgnoreCase(contentType)) {
            // Some clients may not set correct content type; we keep strict per acceptance criteria
            throw new BadRequestException("Invalid content type. Only .xlsx files are supported.");
        }
    }

    private Map<String, Integer> readHeader(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();
        short lastCell = headerRow.getLastCellNum();
        for (int i = 0; i < lastCell; i++) {
            Cell cell = headerRow.getCell(i);
            if (cell == null) continue;
            String name = cell.getStringCellValue();
            if (name == null) continue;
            String key = name.trim().toLowerCase(Locale.ROOT).replace(" ", "_");
            map.put(key, i);
        }
        return map;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private Integer getIntegerCell(Row row, Integer idx) {
        if (idx == null) return null;
        Cell cell = row.getCell(idx);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case NUMERIC -> (int) Math.round(cell.getNumericCellValue());
            case STRING -> {
                String v = cell.getStringCellValue();
                if (isBlank(v)) yield null;
                try { yield Integer.parseInt(v.trim()); }
                catch (NumberFormatException e) { yield null; }
            }
            case BOOLEAN -> cell.getBooleanCellValue() ? 1 : 0;
            default -> null;
        };
    }

    private Double getDoubleCell(Row row, Integer idx) {
        if (idx == null) return null;
        Cell cell = row.getCell(idx);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case NUMERIC -> cell.getNumericCellValue();
            case STRING -> {
                String v = cell.getStringCellValue();
                if (isBlank(v)) yield null;
                try { yield Double.parseDouble(v.trim()); }
                catch (NumberFormatException e) { yield null; }
            }
            default -> null;
        };
    }

    private String getStringCell(Row row, Integer idx) {
        if (idx == null) return null;
        Cell cell = row.getCell(idx);
        if (cell == null) return null;
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                LocalDateTime ldt = cell.getDateCellValue().toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
                return ldt.toString();
            } else {
                double d = cell.getNumericCellValue();
                long asLong = Math.round(d);
                if (Math.abs(d - asLong) < 1e-9) {
                    return Long.toString(asLong);
                }
                return Double.toString(d);
            }
        }
        if (cell.getCellType() == CellType.BOOLEAN) {
            return Boolean.toString(cell.getBooleanCellValue());
        }
        if (cell.getCellType() == CellType.FORMULA) {
            try {
                return cell.getStringCellValue();
            } catch (IllegalStateException ex) {
                try {
                    return Double.toString(cell.getNumericCellValue());
                } catch (IllegalStateException e2) {
                    return null;
                }
            }
        }
        return null;
    }

    private int safeAdd(Integer a, Integer b) {
        int aa = a == null ? 0 : a;
        int bb = b == null ? 0 : b;
        return aa + bb;
    }

    private double safeAdd(Double a, Double b) {
        double aa = a == null ? 0.0 : a;
        double bb = b == null ? 0.0 : b;
        return aa + bb;
    }

    private double clampRate(double v) {
        if (v < 0) return 0;
        if (v > 100) return 100;
        return Math.round(v * 100.0) / 100.0; // 2dp
    }
}
