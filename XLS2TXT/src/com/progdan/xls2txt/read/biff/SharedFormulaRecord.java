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

package com.progdan.xls2txt.read.biff;

import java.util.ArrayList;

import com.progdan.xls2txt.common.Logger;

import com.progdan.xls2txt.Cell;
import com.progdan.xls2txt.CellType;
import com.progdan.xls2txt.CellReferenceHelper;
import com.progdan.xls2txt.biff.FormattingRecords;
import com.progdan.xls2txt.biff.IntegerHelper;
import com.progdan.xls2txt.biff.WorkbookMethods;
import com.progdan.xls2txt.biff.formula.ExternalSheet;

/**
 * A shared formula
 */
class SharedFormulaRecord
{
  /**
   * The logger
   */
  private static Logger logger = Logger.getLogger(SharedFormulaRecord.class);

  /**
   * The first row to which this shared formula applies
   */
  private int firstRow;

  /**
   * The last row to which this shared formula applies
   */
  private int lastRow;

  /**
   * The first column to which this shared formula applies
   */
  private int firstCol;

  /**
   * The last column to which this shared formula applies
   */
  private int lastCol;

  /**
   * The first (template) formula comprising this group
   */
  private BaseSharedFormulaRecord templateFormula;

  /**
   * The rest of the  comprising this shared formula
   */
  private ArrayList formulas;

  /**
   * The token data
   */
  private byte[] tokens;

  /**
   * A handle to the external sheet
   */
  private ExternalSheet externalSheet;

  /**
   * A handle to the name table
   */
  private WorkbookMethods nameTable;

  /**
   * A handle to the sheet
   */
  private SheetImpl sheet;


  /**
   * Constructs this object from the raw data.  Creates either a
   * NumberFormulaRecord or a StringFormulaRecord depending on whether
   * this formula represents a numerical calculation or not
   *
   * @param t the raw data
   * @param fr the base shared formula
   * @param es the workbook, which contains the external sheet references
   * @param nt the workbook
   * @param si the sheet
   */
  public SharedFormulaRecord(Record t, BaseSharedFormulaRecord fr,
                             ExternalSheet es, WorkbookMethods nt,
                             SheetImpl si)
  {
    externalSheet = es;
    nameTable = nt;
    sheet = si;
    byte[] data = t.getData();

    firstRow = IntegerHelper.getInt(data[0], data[1]);
    lastRow = IntegerHelper.getInt(data[2], data[3]);
    firstCol = (int) (data[4] & 0xff);
    lastCol = (int) (data[5] & 0xff);

    formulas = new ArrayList();

    templateFormula = fr;

    tokens = new byte[data.length - 10];
    System.arraycopy(data, 10, tokens, 0, tokens.length);
  }

  /**
   * Adds this formula to the list of formulas, if it falls within
   * the bounds
   *
   * @param fr the formula record to test for membership of this group
   * @return TRUE if the formulas was added, FALSE otherwise
   */
  public boolean add(BaseSharedFormulaRecord fr)
  {
    if (fr.getRow() >= firstRow && fr.getRow() <= lastRow &&
        fr.getColumn() >= firstCol && fr.getColumn() <= lastCol)
    {
      formulas.add(fr);
      return true;
    }

    return false;
  }

  /**
   * Manufactures individual cell formulas out the whole shared formula
   * debacle
   *
   * @param fr the formatting records
   * @param nf flag indicating whether this uses the 1904 date system
   * @return an array of formulas to be added to the sheet
   */
  Cell[] getFormulas(FormattingRecords fr, boolean nf)
  {
    Cell[] sfs = new Cell[formulas.size() + 1];

    // This can happen if there are many identical formulas in the
    // sheet and excel has not sliced and diced them exclusively
    if (templateFormula == null)
    {
      logger.warn("Shared formula template formula is null");
      return new Cell[0];
    }

    templateFormula.setTokens(tokens);

    // See if the template formula evaluates to date
    if (templateFormula.getType() == CellType.NUMBER_FORMULA)
    {
      if (fr.isDate(templateFormula.getXFIndex()))
      {
        SharedNumberFormulaRecord snfr = (SharedNumberFormulaRecord)
                                         templateFormula;
        templateFormula = new SharedDateFormulaRecord(snfr, fr, nf, sheet,
                                                      snfr.getFilePos());
        templateFormula.setTokens(snfr.getTokens());
      }
    }

    sfs[0] = templateFormula;

    BaseSharedFormulaRecord f = null;

    for (int i = 0; i < formulas.size(); i++)
    {
      f = (BaseSharedFormulaRecord) formulas.get(i);

      // See if the formula evaluates to date
      if (f.getType() == CellType.NUMBER_FORMULA)
      {
        if (fr.isDate(f.getXFIndex()))
        {
          SharedNumberFormulaRecord snfr = (SharedNumberFormulaRecord) f;
          f = new SharedDateFormulaRecord(snfr, fr, nf, sheet,
                                          snfr.getFilePos());
        }
      }

      f.setTokens(tokens);
      sfs[i + 1] = f;
    }

    return sfs;
  }

  /**
   * Accessor for the template formula.  Called when a shared formula has,
   * for some reason, specified an inappropriate range and it is necessary
   * to retrieve the template from a previously available shared formula
   */
  BaseSharedFormulaRecord getTemplateFormula()
  {
    return templateFormula;
  }
}






