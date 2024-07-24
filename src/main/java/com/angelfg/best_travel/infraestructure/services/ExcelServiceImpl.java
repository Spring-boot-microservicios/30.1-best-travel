package com.angelfg.best_travel.infraestructure.services;

import com.angelfg.best_travel.domain.entities.jpa.CustomerEntity;
import com.angelfg.best_travel.domain.repositories.jpa.CustomerRepository;
import com.angelfg.best_travel.infraestructure.abstract_services.ReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Service
@AllArgsConstructor
@Slf4j
public class ExcelServiceImpl implements ReportService {

    private static final String SHEET_NAME = "Customer total sales";
    private static final String FONT_TYPE = "Arial";
    private static final String COLUMN_CUSTOMER_ID = "id";
    private static final String COLUMN_CUSTOMER_NAME = "name";
    private static final String COLUMN_CUSTOMER_PURCHASES = "purchases";
    private static final String REPORTS_PATH_WITH_NAME = "reports/Sales-%s";
    private static final String REPORTS_PATH = "reports";
    private static final String FILE_TYPE= ".xlsx";
    private static final String FILE_NAME= "Sales-%s.xlsx";

    private final CustomerRepository customerRepository;

    @Override
    public byte[] readFile() {

        try {
            this.createReport();

            Path path = Paths.get(REPORTS_PATH, String.format(FILE_NAME, LocalDate.now().getMonth())).toAbsolutePath();
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    private void createReport() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(SHEET_NAME);

        // Columnas
        sheet.setColumnWidth(0, 7000); // posicion 1 con el largo de 5000
        sheet.setColumnWidth(1, 7000);
        sheet.setColumnWidth(2, 7000);

        // Cabecera
        XSSFRow header = sheet.createRow(0);
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.VIOLET.getIndex()); // color morado
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // relleno solido

        // Tipo de fuente
        XSSFFont font = workbook.createFont();
        font.setFontName(FONT_TYPE); // tipo de fuente
        font.setFontHeightInPoints((short) 14); // tamaño de fuente
        font.setBold(true); // En negritas

        // Cabecera con estilo de fuente
        headerStyle.setFont(font); // Configuracion de fuente en el header

        // Nombre, estilo y colocacion del header (id)
        XSSFCell headerCell = header.createCell(0);
        headerCell.setCellValue(COLUMN_CUSTOMER_ID);
        headerCell.setCellStyle(headerStyle);

        // Nombre, estilo y colocacion del header (nombre del cliente)
        headerCell = header.createCell(1);
        headerCell.setCellValue(COLUMN_CUSTOMER_NAME);
        headerCell.setCellStyle(headerStyle);

        // Nombre, estilo y colocacion del header (numero de ventas)
        headerCell = header.createCell(2);
        headerCell.setCellValue(COLUMN_CUSTOMER_PURCHASES);
        headerCell.setCellStyle(headerStyle);

        // Crear el estilo con woorbook
        XSSFCellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        Iterable<CustomerEntity> customers = this.customerRepository.findAll();
        int rowPos = 1;

        for (CustomerEntity customer : customers) {
            XSSFRow row = sheet.createRow(rowPos);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue(customer.getDni());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(customer.getFullName());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(getTotalPurchase(customer));
            cell.setCellStyle(style);

            rowPos++;
        }

        File report = new File(String.format(REPORTS_PATH_WITH_NAME, LocalDate.now().getMonth()));
        String path = report.getAbsolutePath(); // Es el path total desde el sistema operativo
        String fileLocation = path + FILE_TYPE;

        try (FileOutputStream outputStream = new FileOutputStream(fileLocation)) {
            workbook.write(outputStream); // Guardado archivo
            // workbook.close(); // Cerrar el buffer (si esta dentro del try lo cierra automatico)
            workbook.close();
        } catch (IOException e) {
            log.error("Cant create Excel", e);
            throw new RuntimeException();
        }

    }

    private static int getTotalPurchase(CustomerEntity customer) {
        return customer.getTotalLodgings() + customer.getTotalFlights() + customer.getTotalTours();
    }

}
