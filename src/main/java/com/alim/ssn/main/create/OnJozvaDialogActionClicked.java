package com.alim.ssn.main.create;

import java.io.File;
import java.util.List;

public interface OnJozvaDialogActionClicked {
   void onImageClicked();
   void onFileClicked();
   void onPostClicked(String desc, List<String> tags);
}
