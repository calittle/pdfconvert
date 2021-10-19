import org.apache.commons.cli.*;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.SimpleRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.List;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        String inputFile="";
        int resolution=0;
        String outputType = "png";

        Options opts = new Options();

        Option opt = new Option("i","inputFile",true,"Input PDF file");
        opt.setRequired(true);
        opts.addOption(opt);

        opt = new Option("o","outputType",true,"Output Filetype");
        opt.setRequired(true);
        opts.addOption(opt);

        opts.addOption(new Option("r","resolution",true,"Desired output resolution"));

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {

            cmd = parser.parse(opts, args);
            inputFile = cmd.getOptionValue("i");
            resolution = Integer.parseInt(cmd.getOptionValue("r"));
            outputType = cmd.getOptionValue("o").toLowerCase(Locale.ROOT);

        } catch (ParseException e) {
            formatter.printHelp("pdfconvert", opts);
            System.exit(1);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }


            PDFDocument pdf = new PDFDocument();
            SimpleRenderer renderer = new SimpleRenderer();
            renderer.setResolution(resolution);
            try {
                pdf.load(new File(inputFile));
            }catch (Exception e) {
                e.printStackTrace();
                System.err.println("Input file <" + inputFile + "> not found or inaccessible.");
                System.exit(1);
            }

            try {
                List<Image> images = renderer.render(pdf);
                for (int i=0; i<images.size(); i++){
                    ImageIO.write((RenderedImage) images.get(i), outputType,new File(inputFile + "_" + i+1 + "." + outputType));
                }
            }
            catch (Exception e){
                e.printStackTrace();
                System.err.println("Unable to render images.");
                System.exit(2);
            }

    }
}
