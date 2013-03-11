


import java.awt.*;
import java.awt.datatransfer.*;
import java.io.ByteArrayOutputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;


public class ClipboardListener extends Thread implements ClipboardOwner {
Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
boolean bEnough=false;
public static String last;
public static JFrame jf = new JFrame();
public static JLabel label2 = new JLabel();



public void run() {
Transferable trans = sysClip.getContents(this);
regainOwnership(trans);
System.out.println("Listening to board...");
while(true) {
if(isitEnough())break;
}
System.out.println("No more Listening...");
}

public void itisEnough(){
bEnough=true;
}
public void itisNotEnough(){
bEnough=false;
}
boolean isitEnough(){
return bEnough;
}
public void lostOwnership(Clipboard c, Transferable t) {
try{
sleep(200);
}catch(Exception e){
System.out.println("Exception: "+e);
}
try{
Transferable contents = c.getContents(this); //EXCEPTION
//processContents(contents);
regainOwnership(contents);
}catch(Exception e){e.printStackTrace();}
}

void processContents(Transferable t) {
if(isitEnough())return;
DataFlavor[] flavors=t.getTransferDataFlavors();
for(int i=flavors.length-1;i>=0;i--){
try{
Object o=t.getTransferData(flavors[i]);


	
	
//System.out.println("Flavor "+i+" gives "+o.getClass().getName());
if(o instanceof String){
///System.out.println("String="+(String)o);
//TODO: here.
	System.out.println(o);
	if ((String)o == last){
		last = (String)o;
		System.out.println("ya");
		continue;
	}
	last = (String)o;
	ByteArrayOutputStream qr = QRCode.from((String)o).to(ImageType.PNG).stream();
	int size = qr.size();
	
	System.out.println(size);
	size/= 3;
	size=(size > Toolkit.getDefaultToolkit().getScreenSize().height-60?Toolkit.getDefaultToolkit().getScreenSize().height-60:size);
	size=(size > Toolkit.getDefaultToolkit().getScreenSize().height-40?Toolkit.getDefaultToolkit().getScreenSize().width-40:size);
	qr = QRCode.from((String)o).withSize(size, size).to(ImageType.PNG).stream();
	Image warnImage = Toolkit.getDefaultToolkit().createImage(
			qr.toByteArray());
	Icon warnIcon = new ImageIcon(warnImage);
    label2.setIcon(warnIcon);
    jf.pack();
	
    //jf.g.drawImage(bim, 0, 0, null);
	
break;
}
}catch(Exception exp){exp.printStackTrace();}
}
///System.out.println("Processing: ");
}

void regainOwnership(Transferable t) {
sysClip.setContents(t, this);
processContents(t);
}

public static void main(String[] args) {
	ClipboardListener.jf.add(label2);
	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jf.setAlwaysOnTop(true);
	jf.setVisible(true);
	ClipboardListener b = new ClipboardListener();
	b.itisNotEnough();
	b.start();
	b.itisNotEnough();

}

}
