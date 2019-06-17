package br.com.bwsystemssolutions.controlediabetes.data;

import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class BackupHelper {

    private static void importDataBase(String backupDBPath, String currentDBPath) throws IOException {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                //String currentDBPath = "//data//" + "<nome do package>"
                //        + "//databases//" + "<nome da BD>";
                //String backupDBPath = "<nome ficheiro backup da BD>"; // No SDCard
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }

    }

    public static void exportDataBase(String currentDBPath, String backupDBPath) throws IOException {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                //String currentDBPath = "//data//" + "<nome do package>"
                //        + "//databases//" + "<nome da BD>";
                //String backupDBPath = "<destino>";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
    }
}
