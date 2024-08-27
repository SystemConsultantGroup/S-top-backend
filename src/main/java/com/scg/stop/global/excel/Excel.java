package com.scg.stop.global.excel;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;

@AllArgsConstructor
public class Excel {
    public String filename;
    public Workbook data;
}
