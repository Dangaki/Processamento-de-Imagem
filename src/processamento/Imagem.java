//Daniel Akira Nakamura Gullich
package processamento;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Imagem {
        private BufferedImage imagemOriginal;
        private BufferedImage imagemEditada;
        private BufferedImage imagemSomaM;
        private BufferedImage imagemSomaP;
        private BufferedImage imagemSub;

    public void criarNovaImagem(int width,int height){
        imagemEditada = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagemEditada.createGraphics();
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, imagemEditada.getWidth(), imagemEditada.getHeight());
        g2d.dispose();
    }
        
    public BufferedImage getImagemSub() {
        return imagemSub;
    }

    public void setImagemSub(BufferedImage imagemSub) {
        this.imagemSub = imagemSub;
    }

    public BufferedImage getImagemSomaP() {
        return imagemSomaP;
    }

    public void setImagemSomaP(BufferedImage imagemSomaP) {
        this.imagemSomaP = imagemSomaP;
    }

    public BufferedImage getImagemSomaM() {
        return imagemSomaM;
    }

    public void setImagemSomaM(BufferedImage imagemSomaM) {
        this.imagemSomaM = imagemSomaM;
    }
        
    
    public BufferedImage getImagemOriginal() {
        return imagemOriginal;
    }

    public void setImagemOriginal(BufferedImage imagemOriginal) {
        this.imagemOriginal = imagemOriginal;
        imagemEditada = copiar(imagemOriginal);  
    }

    public BufferedImage getImagemEditada() {
        return imagemEditada;
    }

    public void setImagemEditada(BufferedImage imagemEditada) {
        this.imagemEditada = imagemEditada;
    }
    
    public static BufferedImage copiar(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
    
    public void restaurar(){
        imagemEditada = copiar(imagemOriginal); 
    }
    
    public void cinzaSimples(){
        int pixel, r ,g ,b ,resultado;
        
        for(int j = 0; j < imagemOriginal.getHeight(); j++){
            for(int i = 0; i < imagemOriginal.getWidth();i++){
                
                pixel = imagemOriginal.getRGB(i,j);
                
                Color cor = new Color(pixel, true);
                
                r = cor.getRed();
                g = cor.getGreen();
                b = cor.getBlue();
                
                resultado = (r + g + b) / 3;
                
                cor = new Color(resultado, resultado, resultado);
                
                imagemEditada.setRGB(i,j, cor.getRGB());
                
            } 
        }
    
    }

    public void cinzaPonderado(){
        int pixel, r ,g ,b;
        double resultado;
        
        for(int j = 0; j < imagemOriginal.getHeight(); j++){
            for(int i = 0; i < imagemOriginal.getWidth();i++){
                
                pixel = imagemOriginal.getRGB(i,j);
                
                Color cor = new Color(pixel, true);
                
                r = cor.getRed();
                g = cor.getGreen();
                b = cor.getBlue();
                
                resultado = (r * 0.299) + (g * 0.587 ) + (b * 0.114);
                
                cor = new Color((int)resultado, (int)resultado, (int)resultado);
                
                imagemEditada.setRGB(i,j, cor.getRGB());
                
            } 
        }
    
    }    
      
    public void limiarizacao(double limite){
        cinzaPonderado();
        
        double pixelLimite = limite * 255;
        
        int pixel, r;
        int resultado;
        
        for(int j = 0; j < imagemOriginal.getHeight(); j++){
            for(int i = 0; i < imagemOriginal.getWidth();i++){
                
                pixel = imagemEditada.getRGB(i,j);
                
                Color cor = new Color(pixel, true);
                
                r = cor.getRed();
                
                if(r > pixelLimite){
                    resultado = 255;
                }
                else
                    resultado = 0;
                
                
                cor = new Color(resultado, resultado, resultado);
                
                imagemEditada.setRGB(i,j, cor.getRGB());
                
            } 
        }
    
    }  
  
    public void negativa(){
        int pixel, r ,g ,b;
        
        for(int j = 0; j < imagemOriginal.getHeight(); j++){
            for(int i = 0; i < imagemOriginal.getWidth();i++){
                
                pixel = imagemOriginal.getRGB(i,j);
                
                Color cor = new Color(pixel, true);
                
                r = 255 - cor.getRed();
                g = 255 - cor.getGreen();
                b = 255 - cor.getBlue();
                
                cor = new Color(r, g, b);
                
                imagemEditada.setRGB(i,j, cor.getRGB());
                
            } 
        }
    
    }
    
    public void somaM(){
        
        int pixelOriginal, pixelSomado, r, g, b, hMenor, wMenor;
        
        if(imagemOriginal.getHeight() <= imagemSomaM.getHeight()){
            hMenor = imagemOriginal.getHeight();
        }
        else{
            hMenor = imagemSomaM.getHeight();
        }
        
        if(imagemOriginal.getWidth() <= imagemSomaM.getWidth()){
            wMenor = imagemOriginal.getWidth();
        }
        else{
            wMenor = imagemSomaM.getWidth();
        } 
        
        for(int j = 0; j < hMenor; j++){
            for(int i = 0; i < wMenor;i++){
                
                pixelOriginal = imagemOriginal.getRGB(i,j);
                pixelSomado = imagemSomaM.getRGB(i,j);
                
                Color corSomado = new Color(pixelSomado, true);
                Color corOriginal = new Color(pixelOriginal, true);
                
                r = (corSomado.getRed() + corOriginal.getRed()) / 2;
                g = (corSomado.getGreen() + corOriginal.getGreen()) / 2;
                b = (corSomado.getBlue() + corOriginal.getBlue() )/ 2;
                  
                corOriginal = new Color(r, g, b);
                
                imagemEditada.setRGB(i,j, corOriginal.getRGB());
                
            } 
        }     
    }
    
    public void somaP(double limitePrincipal){
        int pixelOriginal, pixelSomado, hMenor, wMenor;
        double limiteSecundario = 1.0 - limitePrincipal;
        double r, g, b;
        
        if(imagemOriginal.getHeight() <= imagemSomaP.getHeight()){
            hMenor = imagemOriginal.getHeight();
        }
        else{
            hMenor = imagemSomaP.getHeight();
        }
        
        if(imagemOriginal.getWidth() <= imagemSomaP.getWidth()){
            wMenor = imagemOriginal.getWidth();
        }
        else{
            wMenor = imagemSomaP.getWidth();
        } 
        
        for(int j = 0; j < hMenor; j++){
            for(int i = 0; i < wMenor;i++){
                
                pixelOriginal = imagemOriginal.getRGB(i,j);
                pixelSomado = imagemSomaP.getRGB(i,j);
                
                Color corSomado = new Color(pixelSomado, true);
                Color corOriginal = new Color(pixelOriginal, true);
                
                r = (corSomado.getRed()* limiteSecundario) + (corOriginal.getRed() * limitePrincipal);
                g = (corSomado.getGreen()* limiteSecundario) + (corOriginal.getGreen() * limitePrincipal);
                b = (corSomado.getBlue()* limiteSecundario) + (corOriginal.getBlue() * limitePrincipal);
                  
                corOriginal = new Color((int)r, (int)g, (int)b);
                
                imagemEditada.setRGB(i,j, corOriginal.getRGB());
                
            } 
        }     
    }
    
    public void sub(){
        int pixelOriginal, pixelSub, r, g, b, hMenor, wMenor;
        
        if(imagemOriginal.getHeight() <= imagemSub.getHeight()){
            hMenor = imagemOriginal.getHeight();
        }
        else{
            hMenor = imagemSub.getHeight();
        }
        
        if(imagemOriginal.getWidth() <= imagemSub.getWidth()){
            wMenor = imagemOriginal.getWidth();
        }
        else{
            wMenor = imagemSub.getWidth();
        } 
        
        for(int j = 0; j < hMenor; j++){
            for(int i = 0; i < wMenor;i++){
                
                pixelOriginal = imagemOriginal.getRGB(i,j);
                pixelSub = imagemSub.getRGB(i,j);
                
                Color corSub = new Color(pixelSub, true);
                Color corOriginal = new Color(pixelOriginal, true);
                
                r = corOriginal.getRed() - corSub.getRed();
                g = corOriginal.getGreen() - corSub.getGreen();
                b = corOriginal.getBlue() - corSub.getBlue();
                
                if(r < 0)
                    r = 0;
                if(g < 0)
                    g = 0;
                if(b < 0)
                    b = 0;
                            
                corOriginal = new Color(r, g, b);
                
                imagemEditada.setRGB(i,j, corOriginal.getRGB());
                
            } 
        }     
    }    
    
    public void saliente(){
        int pixel, r;
        
        cinzaPonderado();
        
        int[][] mascara = {{-1,-1,-1},{-1, 8,-1},{-1,-1,-1}};
        int[][] matrixImagem = new int[imagemOriginal.getWidth()][imagemOriginal.getHeight()];
        
        for(int j = 0; j < imagemOriginal.getHeight(); j++){
            for(int i = 0; i < imagemOriginal.getWidth();i++){
                
                pixel = imagemEditada.getRGB(i,j);
                
                Color cor = new Color(pixel, true);
                
                r = cor.getRed();
                
                matrixImagem[i][j] = r;
 
            } 
        }
       
        for(int j = 1; j < imagemOriginal.getHeight() - 1; j++){
              
            for(int i = 1; i < imagemOriginal.getWidth() - 1;i++){
               
               int total = 0;
                
               for(int x = -1 ; x < 2 ; x++){
                      for(int y = -1 ; y < 2 ; y++){
                        total = total  + (matrixImagem[i - x][j - y] * mascara[x + 1][y + 1]);
                    }
                }
               
                if(total < 0){
                    total = 0;
                }
                
                if(total > 255){
                    total = 255;
                }
                
                Color cor = new Color(total, total, total);
                
                imagemEditada.setRGB(i,j, cor.getRGB());
               
            }
        }   
    }
    
    public BufferedImage dilatacao(BufferedImage origem){
        int pixel, r;
        
        BufferedImage nova = new BufferedImage(origem.getWidth(), origem.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = nova.createGraphics();
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, nova.getWidth(), nova.getHeight());
        g2d.dispose();
        
        int[][] matrixImagem = new int[origem.getWidth()][origem.getHeight()];
        int[][] mascara = {{1, 1, 1, 0, 0, 0, 0},
                           {1, 1, 1, 0, 0, 0, 0},
                           {1, 1, 1, 0, 0, 0, 0},
                           {1, 1, 1, 1, 1, 1, 1},
                           {0, 0, 0, 0, 1, 1, 1},
                           {0, 0, 0, 0, 1, 1, 1},
                           {0, 0, 0, 0, 1, 1, 1}};
        
        for(int j = 0; j < origem.getHeight(); j++){
            for(int i = 0; i < origem.getWidth();i++){
                
                pixel = origem.getRGB(i,j);
                
                Color cor = new Color(pixel, true);
                
                r = cor.getRed();
                
                matrixImagem[i][j] = r;
                //System.out.println(r);
 
            } 
        }
         
        
        Color cor;
        for(int j = 3; j < origem.getHeight() - 4; j++){
              
            for(int i = 3; i < origem.getWidth() - 4;i++){
                    
                if(matrixImagem[i][j] != 0){
                    
                    cor = new Color(matrixImagem[i][j], matrixImagem[i][j], matrixImagem[i][j]);
                     
                    for(int x = -3; x < 4 ; x++){
                      for(int y = -3 ; y < 4 ; y++){
                          
                          if(mascara[x + 3][y +3] == 1){
                              
                            nova.setRGB(i - x,j - y, cor.getRGB());
                          
                          }
                      }
                    }  
                }
            }
        }
        
        return nova;
    }
    
    
    public BufferedImage erosao(BufferedImage origem){
        int pixel, r;
        
        BufferedImage nova = new BufferedImage(origem.getWidth(), origem.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = nova.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, nova.getWidth(), nova.getHeight());
        g2d.dispose();
        
        int[][] matrixImagem = new int[origem.getWidth()][origem.getHeight()];
        int[][] mascara = {{0, 0, 0, 0, 1, 1, 1},
                           {0, 0, 0, 0, 1, 1, 1},
                           {0, 0, 0, 0, 1, 1, 1},
                           {1, 1, 1, 1, 1, 1, 1},
                           {1, 1, 1, 0, 0, 0, 0},
                           {1, 1, 1, 0, 0, 0, 0},
                           {1, 1, 1, 0, 0, 0, 0}};
        
        for(int j = 0; j < origem.getHeight(); j++){
            for(int i = 0; i < origem.getWidth();i++){
                
                pixel = origem.getRGB(i,j);
                
                Color cor = new Color(pixel, true);
                
                r = cor.getRed();
                
                matrixImagem[i][j] = r;
                //System.out.println(r);
 
            } 
        }
         
        
        Color cor;
        for(int j = 4; j < origem.getHeight() - 4; j++){
              
            for(int i = 4; i < origem.getWidth() - 4;i++){
                    
                if(matrixImagem[i][j] != 255){
                    
                    cor = new Color(matrixImagem[i][j], matrixImagem[i][j], matrixImagem[i][j]);
                     
                    for(int x = -3; x < 4 ; x++){
                      for(int y = -3 ; y < 4 ; y++){
                          
                          if(mascara[x + 3][y +3] == 1){
                              
                            nova.setRGB(i - x,j - y, cor.getRGB());
                          
                          }
                      }
                    }  
                }
            }
        }
        
        return nova;
    }
    
    public void roberts(){
        int pixel, r;
        double gx, gy, gfinal;
        
        cinzaPonderado();
        
        int[][] mascaraX = {{0,1},{-1, 0}};
        int[][] mascaraY = {{1,0},{0, -1}};
        int[][] matrixImagem = new int[imagemOriginal.getWidth()][imagemOriginal.getHeight()];
        
        for(int j = 0; j < imagemOriginal.getHeight(); j++){
            for(int i = 0; i < imagemOriginal.getWidth();i++){
                
                pixel = imagemOriginal.getRGB(i,j);
                
                Color cor = new Color(pixel, true);
                
                r = cor.getRed();
                
                matrixImagem[i][j] = r;
 
            } 
        }
        
        criarNovaImagem(imagemOriginal.getWidth(),imagemOriginal.getHeight());
       
        for(int j = 0; j < imagemOriginal.getHeight() - 1; j++){
              
            for(int i = 0; i < imagemOriginal.getWidth() - 1;i++){
               
                gx=0;
                
                gy=0;
                
               for(int x = 0 ; x < 2 ; x++){
                      for(int y = 0; y < 2 ; y++){
                        gx = gx  + (matrixImagem[i + x][j + y] * mascaraX[x][y]);
                        gy = gy  + (matrixImagem[i + x][j + y] * mascaraY[x][y]);
                    }
                }
                
                gfinal = Math.sqrt((gx*gx)+(gy*gy));
               
               
                if(gfinal < 0){
                    gfinal = 0;
                }
                
                if(gfinal > 255){
                    gfinal = 255;
                }
                
                Color cor = new Color((int)gfinal, (int)gfinal, (int)gfinal);
                
                imagemEditada.setRGB(i,j, cor.getRGB());
               
            }
        }   
    }
  
    public void sobel(){
        int pixel, r;
        double gx, gy, gfinal;
        
        cinzaPonderado();
        
        int[][] mascaraX = {{ 1,  2,  1},
                            { 0,  0,  0},
                            {-1, -2, -1}};
        int[][] mascaraY = {{1, 0, -1},
                            {2, 0, -2},
                            {1, 0, -1}};
        int[][] matrixImagem = new int[imagemOriginal.getWidth()][imagemOriginal.getHeight()];
        
        for(int j = 0; j < imagemOriginal.getHeight(); j++){
            for(int i = 0; i < imagemOriginal.getWidth();i++){
                
                pixel = imagemOriginal.getRGB(i,j);
                
                Color cor = new Color(pixel, true);
                
                r = cor.getRed();
                
                matrixImagem[i][j] = r;
 
            } 
        }
        
        criarNovaImagem(imagemOriginal.getWidth(),imagemOriginal.getHeight());
       
        for(int j = 1; j < imagemOriginal.getHeight() - 1; j++){
              
            for(int i = 1; i < imagemOriginal.getWidth() - 1;i++){
               
                gx=0;
                
                gy=0; 
               
               for(int x = -1 ; x < 2 ; x++){
                      for(int y = -1 ; y < 2 ; y++){
                        gx = gx  + (matrixImagem[i - x][j - y] * mascaraX[x + 1][y + 1]);
                        gy = gy  + (matrixImagem[i - x][j - y] * mascaraY[x + 1][y + 1]);
                    }
                } 
               
               
                gfinal = Math.sqrt((gx*gx)+(gy*gy));
               
               
                if(gfinal < 0){
                    gfinal = 0;
                }
                
                if(gfinal > 255){
                    gfinal = 255;
                }
                
                Color cor = new Color((int)gfinal, (int)gfinal, (int)gfinal);
                
                imagemEditada.setRGB(i,j, cor.getRGB());
               
            }
        }   
    }
    
    public void robinson(){
        int pixel, r;
        double g1, g2, g3, g4, g5, g6, g7, g8, gfinal;
        
        cinzaPonderado();
        
        int[][] mascara1 = {{ 1,  2,  1},
                            { 0,  0,  0},
                            {-1, -2, -1}};
        
        int[][] mascara2 = {{-1, -2, -1},
                            { 0,  0,  0},
                            { 1,  2,  1}};
        
        int[][] mascara3 = {{1, 0, -1},
                            {2, 0, -2},
                            {1, 0, -1}};
        
        int[][] mascara4 = {{-1, 0, 1},
                            {-2, 0, 2},
                            {-1, 0, 1}};
        
        int[][] mascara5 = {{2,  1,  0},
                            {1,  0, -1},
                            {0, -1, -2}};
        
        int[][] mascara6 = {{-2, -1,  0},
                            {-1,  0,  1},
                            { 0,  1,  2}};
        
        int[][] mascara7 = {{0, -1,  -2},
                            {1,  0,  -1},
                            {2,  1,   0}};
        
        int[][] mascara8 = {{ 0,  1,  2},
                            {-1,  0,  1},
                            {-2, -1,  0}};
        
        int[][] matrixImagem = new int[imagemOriginal.getWidth()][imagemOriginal.getHeight()];
        
        for(int j = 0; j < imagemOriginal.getHeight(); j++){
            for(int i = 0; i < imagemOriginal.getWidth();i++){
                
                pixel = imagemOriginal.getRGB(i,j);
                
                Color cor = new Color(pixel, true);
                
                r = cor.getRed();
                
                matrixImagem[i][j] = r;
 
            } 
        }
        
        criarNovaImagem(imagemOriginal.getWidth(),imagemOriginal.getHeight());
       
        for(int j = 1; j < imagemOriginal.getHeight() - 1; j++){
              
            for(int i = 1; i < imagemOriginal.getWidth() - 1;i++){
               
               g1=0;
               g2=0;
               g3=0;
               g4=0;
               g5=0;
               g6=0;
               g7=0;
               g8=0;
               
               for(int x = -1 ; x < 2 ; x++){
                      for(int y = -1 ; y < 2 ; y++){
                        g1 = g1  + (matrixImagem[i - x][j - y] * mascara1[x + 1][y + 1]);
                        g2 = g2  + (matrixImagem[i - x][j - y] * mascara2[x + 1][y + 1]);
                        g3 = g3  + (matrixImagem[i - x][j - y] * mascara3[x + 1][y + 1]);
                        g4 = g4  + (matrixImagem[i - x][j - y] * mascara4[x + 1][y + 1]);
                        g5 = g5  + (matrixImagem[i - x][j - y] * mascara5[x + 1][y + 1]);
                        g6 = g6  + (matrixImagem[i - x][j - y] * mascara6[x + 1][y + 1]);
                        g7 = g7  + (matrixImagem[i - x][j - y] * mascara7[x + 1][y + 1]);
                        g8 = g8  + (matrixImagem[i - x][j - y] * mascara8[x + 1][y + 1]);
                    }
                } 
               
               
                gfinal = Math.sqrt((g1*g1)+(g2*g2)+(g3*g3)+(g4*g4)+(g5*g5)+(g6*g6)+(g7*g7)+(g8*g8));
               
               
                if(gfinal < 0){
                    gfinal = 0;
                }
                
                if(gfinal > 255){
                    gfinal = 255;
                }
                
                Color cor = new Color((int)gfinal, (int)gfinal, (int)gfinal);
                
                imagemEditada.setRGB(i,j, cor.getRGB());
               
            }
        }   
    }
    
     
}
