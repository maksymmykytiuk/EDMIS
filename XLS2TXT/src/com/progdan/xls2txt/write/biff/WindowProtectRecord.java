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

package com.progdan.xls2txt.write.biff;

import com.progdan.xls2txt.biff.Type;
import com.progdan.xls2txt.biff.IntegerHelper;
import com.progdan.xls2txt.biff.WritableRecordData;

/**
 * Stores an option from the Protect Workbook dialog box
 */
class WindowProtectRecord extends WritableRecordData
{
  /**
   * Protect flag
   */
  private boolean protection;
  /**
   * The binary data
   */
  private byte[] data;

  /**
   * Constructor
   *
   * @param prot the protect flag
   */
  public WindowProtectRecord(boolean prot)
  {
    super(Type.WINDOWPROTECT);

    protection = prot;

    data = new byte[2];

    if (protection)
    {
      IntegerHelper.getTwoBytes(1, data, 0);
    }
  }

  /**
   * Gets the binary data for output to file
   *
   * @return the binary data
   */
  public byte[] getData()
  {
    return data;
  }
}
