package com.scg.stop.global.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;

@AllArgsConstructor
@Getter
public class Excel {
    public String filename;
    public SXSSFWorkbook data;

    public void write(OutputStream out) throws IOException {
        this.data.write(out);
        this.data.dispose();
        this.data.close();
    }
}
