/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formulario;

/**
 *
 * @author José Pérez
 */
public class GenerarCodigos_Cp {
    
      public int dato;
    public int cont = 1;
    public String num = "";

  public void generar(int dato) {
        this.dato = dato;
         
           if((this.dato>=999) || (this.dato<9999)) 
           {
               int can=cont+this.dato;
               num = "CP" + can; 
           }
           if((this.dato>=99) || (this.dato<999))
           {
               int can=cont+this.dato;
               num = "CP0"  + can; 
           }
           if((this.dato>=9) && (this.dato<99)) 
           {
                int can=cont+this.dato;
               num = "CP00"  + can; 
           }
           if((this.dato>=1) && (this.dato<9)) 
           {
               int can=cont+this.dato;
               num = "CP000" + can; 
           }
           if(this.dato==0)
           {
              int can=cont+this.dato;
               num = "CP000" + can; 
           }
    }

    public String serie() {
        return this.num;
    }
}
