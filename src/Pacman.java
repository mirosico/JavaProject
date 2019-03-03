import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Pacman extends JFrame {

    public Pacman() {
        
        initUI();
    }
    
    private void initUI() {
        
    	
    	add(new Board());
        setTitle("Obschajnii dvij");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(660, 775);
       setLocationRelativeTo(null);
        setVisible(true);        
    }


    public static void main(String[] args) {

    	
        EventQueue.invokeLater(() -> {
            Pacman ex = new Pacman();
            ex.setVisible(true);
        });
       // music();

    }
   // public static AdvancedPlayer explay;
	public static String muss = "mus.mp3";

	public static void music() {
				try {
					InputStream potok = new FileInputStream(muss);
					//AudioDevice auDev = new JavaSoundAudioDevice();
				//	explay = new AdvancedPlayer(potok, auDev);
				//	explay.play();
				} catch (Exception err) {
					err.printStackTrace();
				}
			
	}
}
