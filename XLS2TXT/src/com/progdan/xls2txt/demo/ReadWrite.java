/*********************************************************************
*
*      Copyright (C) 2002 Andrew Khan
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
***************************************************************************/

package com.progdan.xls2txt.demo;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import java.net.URL;
import java.net.MalformedURLException;

import com.progdan.xls2txt.common.Logger;

import com.progdan.xls2txt.Workbook;
import com.progdan.xls2txt.CellType;
import com.progdan.xls2txt.Cell;
import com.progdan.xls2txt.Sheet;
import com.progdan.xls2txt.FormulaCell;
import com.progdan.xls2txt.Range;
import com.progdan.xls2txt.biff.formula.FormulaException;
import com.progdan.xls2txt.format.CellFormat;
import com.progdan.xls2txt.format.UnderlineStyle;
import com.progdan.xls2txt.format.Colour;
import com.progdan.xls2txt.read.biff.BiffException;
import com.progdan.xls2txt.write.WritableWorkbook;
import com.progdan.xls2txt.write.WritableSheet;
import com.progdan.xls2txt.write.WritableCell;
import com.progdan.xls2txt.write.WritableCellFormat;
import com.progdan.xls2txt.write.WritableFont;
import com.progdan.xls2txt.write.WritableHyperlink;
import com.progdan.xls2txt.write.Label;
import com.progdan.xls2txt.write.NumberFormat;
import com.progdan.xls2txt.write.NumberFormats;
import com.progdan.xls2txt.write.Number;
import com.progdan.xls2txt.write.DateFormat;
import com.progdan.xls2txt.write.DateFormats;
import com.progdan.xls2txt.write.DateTime;
import com.progdan.xls2txt.write.Formula;
import com.progdan.xls2txt.write.WriteException;
import com.progdan.xls2txt.write.WritableImage;


/**
 * Demo class which uses the api to read in a spreadsheet and generate a clone
 * of that spreadsheet which contains the same data.  If the spreadsheet read
 * in is the spreadsheet called jxlrwtest.xls (provided with the distribution)
 * then this class will modify certain fields in the copy of that spreadsheet.
 * This is illustrating that it is possible to read in a spreadsheet, modify
 * a few values, and write it under a new name.
 */
public class ReadWrite
{
  /**
   * The logger
   */
  private static Logger logger = Logger.getLogger(ReadWrite.class);

  /**
   * The spreadsheet to read in
   */
  private File inputWorkbook;
  /**
   * The spreadsheet to output
   */
  private File outputWorkbook;

  /**
   * Constructor
   *
   * @param output
   * @param input
   */
  public ReadWrite(String input, String output)
  {
    inputWorkbook = new File(input);
    outputWorkbook = new File(output);
    logger.setSuppressWarnings(Boolean.getBoolean("com.progdan.xls2txt.nowarnings"));
    logger.info("Input file:  " + input);
    logger.info("Output file:  " + output);
  }

  /**
   * Reads in the inputFile and creates a writable copy of it called outputFile
   *
   * @exception IOException
   * @exception BiffException
   */
  public void readWrite() throws IOException, BiffException, WriteException
  {
    logger.info("Reading...");
    Workbook w1 = Workbook.getWorkbook(inputWorkbook);

    Sheet s = w1.getSheet(0);

    logger.info("Copying...");
    WritableWorkbook w2 = Workbook.createWorkbook(outputWorkbook, w1);

    if (inputWorkbook.getName().equals("jxlrwtest.xls"))
    {
      modify(w2);
    }

    w2.write();
    w2.close();
    logger.info("Done");
  }

  /**
   * If the inputFile was the test spreadsheet, then it modifies certain fields
   * of the writable copy
   *
   * @param w
   */
  private void modify(WritableWorkbook w) throws WriteException
  {
    logger.info("Modifying...");

    WritableSheet sheet = w.getSheet("modified");

    WritableCell cell = null;
    CellFormat cf = null;

    // Change the format of cell B4 to be emboldened
    cell = sheet.getWritableCell(1,3);
    WritableFont bold = new WritableFont(WritableFont.ARIAL,
                                         WritableFont.DEFAULT_POINT_SIZE,
                                         WritableFont.BOLD);
    cf = new WritableCellFormat(bold);
    cell.setCellFormat(cf);

    // Change the format of cell B5 to be underlined
    cell = sheet.getWritableCell(1,4);
    WritableFont underline = new WritableFont(WritableFont.ARIAL,
                                              WritableFont.DEFAULT_POINT_SIZE,
                                              WritableFont.NO_BOLD,
                                              false,
                                              UnderlineStyle.SINGLE);
    cf = new WritableCellFormat(underline);
    cell.setCellFormat(cf);

    // Change the point size of cell B6 to be 10 point
    cell = sheet.getWritableCell(1,5);
    WritableFont tenpoint = new WritableFont(WritableFont.ARIAL, 10);
    cf = new WritableCellFormat(tenpoint);
    cell.setCellFormat(cf);

    // Change the contents of cell B7 to read "Label - mod"
    cell = sheet.getWritableCell(1,6);
    if (cell.getType() == CellType.LABEL)
    {
      Label lc = (Label) cell;
      lc.setString(lc.getString() + " - mod");
    }

    // Change cell B10 to display 7 dps
    cell = sheet.getWritableCell(1,9);
    NumberFormat sevendps = new NumberFormat("#.0000000");
    cf = new WritableCellFormat(sevendps);
    cell.setCellFormat(cf);


    // Change cell B11 to display in the format 1e4
    cell = sheet.getWritableCell(1,10);
    NumberFormat exp4 = new NumberFormat("0.####E0");
    cf = new WritableCellFormat(exp4);
    cell.setCellFormat(cf);

    // Change cell B12 to be normal display
    cell = sheet.getWritableCell(1,11);
    cell.setCellFormat(WritableWorkbook.NORMAL_STYLE);

    // Change the contents of cell B13 to 42
    cell = sheet.getWritableCell(1,12);
    if (cell.getType() == CellType.NUMBER)
    {
      Number n = (Number) cell;
      n.setValue(42);
    }

    // Add 0.1 to the contents of cell B14
    cell = sheet.getWritableCell(1,13);
    if (cell.getType() == CellType.NUMBER)
    {
      Number n = (Number) cell;
      n.setValue(n.getValue() + 0.1);
    }

    // Change the date format of cell B17 to be a custom format
    cell = sheet.getWritableCell(1,16);
    DateFormat df = new DateFormat("dd MMM yyyy HH:mm:ss");
    cf = new WritableCellFormat(df);
    cell.setCellFormat(cf);

    // Change the date format of cell B18 to be a standard format
    cell = sheet.getWritableCell(1,17);
    cf = new WritableCellFormat(DateFormats.FORMAT9);
    cell.setCellFormat(cf);

    // Change the date in cell B19 to be 18 Feb 1998, 11:23:28
    cell = sheet.getWritableCell(1,18);
    if (cell.getType() == CellType.DATE)
    {
      DateTime dt = (DateTime) cell;
      Calendar cal = Calendar.getInstance();
      cal.set(1998, 1, 18, 11, 23, 28);
      Date d = cal.getTime();
      dt.setDate(d);
    }

    // Change the value in B23 to be 6.8.  This should recalculate the
    // formula
    cell = sheet.getWritableCell(1,22);
    if (cell.getType() == CellType.NUMBER)
    {
      Number n = (Number) cell;
      n.setValue(6.8);
    }

    // Change the label in B30.  This will have the effect of making
    // the original string unreferenced
    cell = sheet.getWritableCell(1, 29);
    if (cell.getType() == CellType.LABEL)
    {
      Label l = (Label) cell;
      l.setString("Modified string contents");
    }
    // Insert a new row (number 35)
    sheet.insertRow(34);

    // Delete row 38 (39 after row has been inserted)
    sheet.removeRow(38);

    // Insert a new column (J)
    sheet.insertColumn(9);

    // Remove a column (L - M after column has been inserted)
    sheet.removeColumn(11);

    // Remove row 44 (contains a hyperlink), and then insert an empty
    // row just to keep the numbers consistent
    sheet.removeRow(43);
    sheet.insertRow(43);

    // Modify the hyperlinks
    WritableHyperlink hyperlinks[] = sheet.getWritableHyperlinks();

    for (int i = 0; i < hyperlinks.length; i++)
    {
      WritableHyperlink wh = hyperlinks[i];
      if (wh.getColumn() == 1 && wh.getRow() == 39)
      {
        try
        {
          // Change the hyperlink that begins in cell B40 to be a different API
          wh.setURL(new URL("http://www.andykhan.com/jexcelapi/index.html"));
        }
        catch (MalformedURLException e)
        {
          logger.warn(e.toString());
        }
      }
      else if (wh.getColumn() == 1 && wh.getRow() == 40)
      {
        wh.setFile(new File("../jexcelapi/docs/overview-summary.html"));
      }
      else if (wh.getColumn() == 1 && wh.getRow() == 41)
      {
        wh.setFile(new File("d:/home/jexcelapi/docs/jxl/package-summary.html"));
      }
      else if (wh.getColumn() == 1 && wh.getRow() == 44)
      {
        // Remove the hyperlink at B45
        sheet.removeHyperlink(wh);
      }
    }

    // Change the background of cell F31 from blue to red
    WritableCell c = sheet.getWritableCell(5,30);
    WritableCellFormat newFormat = new WritableCellFormat(c.getCellFormat());
    newFormat.setBackground(Colour.RED);
    c.setCellFormat(newFormat);

    // Modify the contents of the merged cell
    Label l = new Label(0, 49, "Modified merged cells");
    sheet.addCell(l);

    // Modify the chart data
    Number n = (Number) sheet.getWritableCell(0, 70);
    n.setValue(9);

    n = (Number) sheet.getWritableCell(0, 71);
    n.setValue(10);

    n = (Number) sheet.getWritableCell(0, 73);
    n.setValue(4);

    // Add in a cross sheet formula
    Formula f = new Formula(1, 80, "ROUND(COS(original!B10),2)");
    sheet.addCell(f);

    // Add in a formula from the named cells
    f = new Formula(1, 83, "value1+value2");
    sheet.addCell(f);

    // Add in a function formula using named cells
    f = new Formula(1, 84, "AVERAGE(value1,value1*4,value2)");
    sheet.addCell(f);

    // Copy sheet 1 to sheet 3
    //     w.copySheet(0, "copy", 2);

    // Use the cell deep copy feature
    Label label = new Label(0, 88, "Some copied cells", cf);
    sheet.addCell(label);

    label = new Label(0,89, "Number from B9");
    sheet.addCell(label);

    WritableCell wc = sheet.getWritableCell(1, 9).copyTo(1,89);
    sheet.addCell(wc);

    label = new Label(0, 90, "Label from B4 (modified format)");
    sheet.addCell(label);

    wc = sheet.getWritableCell(1, 3).copyTo(1,90);
    sheet.addCell(wc);

    label = new Label(0, 91, "Date from B17");
    sheet.addCell(label);

    wc = sheet.getWritableCell(1, 16).copyTo(1,91);
    sheet.addCell(wc);

    label = new Label(0, 92, "Boolean from E16");
    sheet.addCell(label);

    wc = sheet.getWritableCell(4, 15).copyTo(1,92);
    sheet.addCell(wc);

    label = new Label(0, 93, "URL from B40");
    sheet.addCell(label);

    wc = sheet.getWritableCell(1, 39).copyTo(1,93);
    sheet.addCell(wc);

    // Add some numbers for the formula copy
    for (int i = 0 ; i < 6; i++)
    {
      Number number = new Number(1,94+i, i + 1 + i/8.0);
      sheet.addCell(number);
    }

    label = new Label(0,100, "Formula from B27");
    sheet.addCell(label);

    wc = sheet.getWritableCell(1, 26).copyTo(1,100);
    sheet.addCell(wc);

    label = new Label(0,101, "A brand new formula");
    sheet.addCell(label);

    Formula formula = new Formula(1, 101, "SUM(B94:B96)");
    sheet.addCell(formula);

    label = new Label(0,102, "A copy of it");
    sheet.addCell(label);

    wc = sheet.getWritableCell(1,101).copyTo(1, 102);
    sheet.addCell(wc);

    // Remove the second image from the sheet
    WritableImage wi = sheet.getImage(1);
    sheet.removeImage(wi);

    wi = new WritableImage(1, 116, 2, 9,
                           new File("resources/littlemoretonhall.png"));
    sheet.addImage(wi);
  }
}









