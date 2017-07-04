import java.io.FileWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.NumberFormatException;
import java.io.IOException;

import static jdk.nashorn.internal.objects.Global.Infinity;

class BorderException extends Exception {
    public BorderException(){
    }
}

class ArgsException extends Exception {
    public ArgsException(){
    }
}

public class CelToFahr {
    private static double cel, fahr;

    private static void work() {
        Scanner in = new Scanner(System.in);
        do {
            try {
                System.out.print("Введите значение градусов по цельсию (от -273 до 10 000): ");
                cel = in.nextDouble();
                if (cel < -273 || cel > 10000 || cel == Infinity) {
                    throw new BorderException();
                }
                fahr = cel * 9.0 / 5.0 + 32;
                System.out.println("Температуре " + cel + " по Цельсию соответствует " + fahr + " по Фаренгейту!");
                return;
            } catch (BorderException e) {
                System.out.println("Введённое число находится вне заданных границ, попробуйте ещё раз!");
            } catch (InputMismatchException e) {
                in.nextLine();
                System.out.println("Вы ввели не число, попробуйте ещё раз!");
            }
        } while (true);
    }

    private static void scanAndFileWriterWork(Scanner sc, FileWriter fr) {
        String s = "";
        while (sc.hasNext())
            s += sc.nextLine() + "\r\n";
        sc.close();
        try{
            cel = Double.parseDouble(s);
            if (cel < -273 || cel > 10000 || cel == Infinity) {
                throw new BorderException();
            }
            fahr = cel * 9.0 / 5.0 + 32;
            s="Температуре " + cel + " по Цельсию соответствует " + fahr + " по Фаренгейту!\n";
        }
        catch (BorderException e) {
            s = "Число, находящееся в файле inp находится вне заданных границ!\n";
        } catch (NumberFormatException e) {
            s= "Ошибка, в файле некорректное число!\n";
        }
        finally {
            try{fr.write(s); fr.close();}
            catch (IOException e) {}
        }
    }

    static private Scanner getScanner (String[] args){
        File inp=null;
        Scanner in=null;
        try{
            if (args[0].equals("-i")) {
                inp = new File(args[1]);
                in = new Scanner(inp);
            }
            if (args[2].equals("-i")) {
                inp = new File(args[3]);
                in = new Scanner(inp);
            }
            if (inp==null) {
                throw new ArgsException();
            }
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Аргументы командной строки были введены некорректно!");
        }
        catch (FileNotFoundException e) {
            System.out.println("Файл, из которого будет производиться чтение(input), не был найден!");
        }
        catch (ArgsException e){
            System.out.println("Среди введённых аргументов не было идентификатора -i!");
        }
        finally {
            return in;
        }
    }

    static private FileWriter getFileWritter (String[] args){
        File out=null;
        FileWriter fr = null;
        try{
            if (args[0].equals("-o")) {
                out = new File(args[1]);
                fr = new FileWriter(out);
            }
            if (args[2].equals("-o")) {
                out = new File(args[3]);
                fr = new FileWriter(out);
            }
            if (out==null) {
                throw new ArgsException();
            }
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Аргументы командной строки были введены некорректно!");
        }
        catch (IOException e) {
            System.out.println("Файл, в который будет производится запись(output), не был найден!");
        }
        catch (ArgsException e){
            System.out.println("Среди введённых аргументов не было идентификатора -o!");
        }
        finally {
            return fr;
        }
    }

    static public boolean fileWork (String[] args){
        Scanner sc=getScanner(args);
        FileWriter fr=getFileWritter(args);
        if (sc!=null&&fr!=null){
            CelToFahr.scanAndFileWriterWork(sc,fr);
            System.out.println("Файлы были успешно обработаны!");
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        if(!CelToFahr.fileWork(args)){
            CelToFahr.work();
        }
    }
}