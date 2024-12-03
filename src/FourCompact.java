import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URISyntaxException;

public class FourCompact
{
    public static String path;
    public static void main(String[] args)
    {
        try
        {
            path = FourCompact.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();
        } catch (URISyntaxException e) {throw new RuntimeException(e); }

        try
        {
            encode();
            decode();
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void encode() throws IOException
    {
        File file = new File(path + "/image.png");
        BufferedImage image = ImageIO.read(file);

        int height = image.getHeight();
        int width = image.getWidth();

        BufferedImage newImage = new BufferedImage(width/2, height/2, BufferedImage.TYPE_INT_ARGB);

        for (int rx=1; rx<width-2; rx+=2)
        {
            for (int ry=1; ry<height-2; ry+=2)
            {
                int[] pxcol = new int[4];

                int clr = image.getRGB(rx, ry);
                int red =   (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue =   clr & 0x000000ff;
                pxcol[0] = convertToC255(convertToB6(red),convertToB6(green),convertToB6(blue));

                clr = image.getRGB(rx+1, ry);
                red =   (clr & 0x00ff0000) >> 16;
                green = (clr & 0x0000ff00) >> 8;
                blue =   clr & 0x000000ff;
                pxcol[1] = convertToC255(convertToB6(red),convertToB6(green),convertToB6(blue));

                clr = image.getRGB(rx, ry+1);
                red =   (clr & 0x00ff0000) >> 16;
                green = (clr & 0x0000ff00) >> 8;
                blue =   clr & 0x000000ff;
                pxcol[2] = convertToC255(convertToB6(red),convertToB6(green),convertToB6(blue));

                clr = image.getRGB(rx+1, ry+1);
                red =   (clr & 0x00ff0000) >> 16;
                green = (clr & 0x0000ff00) >> 8;
                blue =   clr & 0x000000ff;
                pxcol[3] = convertToC255(convertToB6(red),convertToB6(green),convertToB6(blue));

                newImage.setRGB(rx/2,ry/2, new Color(pxcol[0],pxcol[1],pxcol[2],pxcol[3]).getRGB());
            }
        }

        File f = new File(path + "/image.compressed.png");
        ImageIO.write(newImage, "PNG", f);
    }

    public static void decode() throws IOException
    {
        File file = new File(path + "/image.compressed.png");
        BufferedImage image = ImageIO.read(file);

        int height = image.getHeight();
        int width = image.getWidth();

        BufferedImage newestImage = new BufferedImage(width*2, height*2, BufferedImage.TYPE_INT_ARGB);


        for (int rx=0; rx<width-1; rx++)
        {
            for (int ry=0; ry<height-1; ry++)
            {
                int clr = image.getRGB(rx, ry);
                int alpha = (clr & 0xff000000) >>>24;
                int red =   (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue =   clr & 0x000000ff;

                int[] px1 = convertFromC255(red);
                int[] px2 = convertFromC255(green);
                int[] px3 = convertFromC255(blue);
                int[] px4 = convertFromC255(alpha);

                newestImage.setRGB(rx*2,ry*2, new Color(px1[0],px1[1],px1[2],255).getRGB());
                newestImage.setRGB(rx*2+1,ry*2, new Color(px2[0],px2[1],px2[2],255).getRGB());
                newestImage.setRGB(rx*2,ry*2+1, new Color(px3[0],px3[1],px3[2],255).getRGB());
                newestImage.setRGB(rx*2+1,ry*2+1, new Color(px4[0],px4[1],px4[2],255).getRGB());
            }
        }

        File f = new File(path + "/image.uncompressed.png");
        ImageIO.write(newestImage, "PNG", f);
    }

    public static int convertToB6(int in)
    {
        if (in <= 42) return 0;
        else if (in <= 84) return 1;
        else if (in <= 126) return 2;
        else if (in <= 168) return 3;
        else if (in <= 210) return 4;
        else if (in <= 255) return 5;
        else return -1;
    }

    public static int convertToC255(int r, int g, int b)
    {
        int a = 1;

        switch (r)
        {
            case 0: a+=0; break;
            case 1: a+=36; break;
            case 2: a+=72; break;
            case 3: a+=108; break;
            case 4: a+=144; break;
            case 5: a+=180; break;
        }

        switch (g)
        {
            case 1: a+=6; break;
            case 2: a+=12; break;
            case 3: a+=18; break;
            case 4: a+=24; break;
            case 5: a+=30; break;
        }

        a+=b;

        return a;
    }

    public static int[] convertFromC255(int in)
    {
        int[] out = new int[3];

        if(in >= 181) { out[0] = 255; in-=181;}
        else if(in >= 144) {out[0] = 204; in-=144;}
        else if(in >= 108) {out[0] = 153; in-=108;}
        else if(in >= 72) {out[0] = 102; in-=72;}
        else if(in >= 36 ) {out[0] = 51; in-=36;}

        if(in >= 30) { out[1] = 255; in-=30;}
        else if(in >= 24) {out[1] = 204; in-=24;}
        else if(in >= 18) {out[1] = 153; in-=18;}
        else if(in >= 12) {out[1] = 102; in-=12;}
        else if(in >= 6) {out[1] = 51; in-=6;}

        switch (in)
        {
            case 5: out[2] = 255; break;
            case 4: out[2] = 204; break;
            case 3: out[2] = 153; break;
            case 2: out[2] = 102; break;
            case 1: out[2] = 51; break;
        }

        return out;
    }
}