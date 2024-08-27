package com.scg.stop.global.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Workbook;

@AllArgsConstructor
@Getter
public class Excel {
    public String filename;
    public Workbook data;
}
