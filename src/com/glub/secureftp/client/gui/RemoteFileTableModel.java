
//*****************************************************************************
//*
//* (c) Copyright 2003. Glub Tech, Incorporated. All Rights Reserved.
//*
//* $Id: RemoteFileTableModel.java 37 2009-05-11 22:46:15Z gary $
//*
//*****************************************************************************

package com.glub.secureftp.client.gui;

import com.glub.secureftp.bean.*;
import com.glub.gui.table.*;

import java.util.*;

public class RemoteFileTableModel extends DefaultSortTableModel {
  protected static final long serialVersionUID = 1L;
  public RemoteFileTableModel() { super(); }

  public RemoteFileTableModel(Vector data, Vector names) {
    super(data, names);
  }
  
  public void sortColumn( int col, boolean ascending ) {
    Collections.sort( getDataVector(), 
                      new RemoteFileComparator(col, ascending) );
  }

  // for some reason out of our control, this exception gets thrown
  public Object getValueAt( int row, int column ) {
    Object o = null;
    try {
      o = super.getValueAt( row, column );
    }
    catch ( ArrayIndexOutOfBoundsException aiobe ) {}
    return o;
  }

  public boolean isCellEditable( int rowIndex, int colIndex ) {
    return false;
  }
}

class RemoteFileComparator extends ColumnComparator {
  public RemoteFileComparator( int index, boolean ascending ) {
    super( index, ascending );
  }

  public int compare( Object one, Object two ) {
    if ( one instanceof Vector && two instanceof Vector ) {
      Vector vOne = (Vector)one;
      Vector vTwo = (Vector)two;

      if ( index < 0 ) { return -1; }

      Object oOne = vOne.elementAt(index);
      Object oTwo = vTwo.elementAt(index);

      if ( oOne.toString().equals("") ) {
        return 0;
      }

      if ( oOne instanceof RemoteFile && oTwo instanceof RemoteFile ) {
        RemoteFile rfOne = (RemoteFile)oOne;
        RemoteFile rfTwo = (RemoteFile)oTwo;

        if ( rfOne.getFileName().equals("..") ) {
          return 0;
        }

        String ftOne = rfOne.getMetaData("fileType") + rfOne.getFileName();
        String ftTwo = rfTwo.getMetaData("fileType") + rfTwo.getFileName();

        if ( ascending ) {
          return ftOne.compareTo( ftTwo );
        }
        else {
          return ftTwo.compareTo( ftOne );
        }
      }
      else if ( oOne instanceof Calendar && oTwo instanceof Calendar ) {
        Calendar cOne = (Calendar)oOne;
        Calendar cTwo = (Calendar)oTwo;
        Date dOne = cOne.getTime();
        Date dTwo = cTwo.getTime();

        if ( ascending ) {
          return dOne.compareTo( dTwo );
        }
        else {
          return dTwo.compareTo( dOne );
        }
      }
      else {
        try {
          if (oOne instanceof Comparable && oTwo instanceof Comparable) {

            Comparable cOne = (Comparable)oOne;
            Comparable cTwo = (Comparable)oTwo;

            if (ascending) {
              return cOne.compareTo(cTwo);
            }
            else {
              return cTwo.compareTo(cOne);
            }
          }
        }
        catch ( ClassCastException cse ) {
          return 0;
        }
      }
    }
    return 1;
  }
}
