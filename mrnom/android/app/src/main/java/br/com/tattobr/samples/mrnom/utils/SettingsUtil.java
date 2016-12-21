package br.com.tattobr.samples.mrnom.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import br.com.tattobr.samples.framework.FileIO;

public class SettingsUtil {
    private static final String SETTINGS_FILE = ".mrnom";
    public static boolean soundEnabled = true;
    public static int[] highscores = new int[]{100, 80, 50, 30, 10};

    public static void load(FileIO files) {
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;
        try {
            inputStream = files.readFile(SETTINGS_FILE);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            soundEnabled = Boolean.parseBoolean(bufferedReader.readLine());
            for (int i = 0; i < 5; i++) {
                highscores[i] = Integer.parseInt(bufferedReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(SETTINGS_FILE)));
            out.write(Boolean.toString(soundEnabled));
            out.newLine();
            for (int i = 0; i < 5; i++) {
                out.write(Integer.toString(highscores[i]));
                out.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void addScore(int score) {
        for (int i = 0; i < 5; i++) {
            if (highscores[i] < score) {
                for (int j = 4; j > i; j--) {
                    highscores[j] = highscores[j - 1];
                }
                highscores[i] = score;
                break;
            }
        }
    }
}
