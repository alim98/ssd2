package com.alim.ssn.main.create;

import java.io.File;

import java.util.List;

public interface PdfCreator {

    File createPdf(List<byte[]> bytes ) ;
}
