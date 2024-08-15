import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class TextEditor implements ActionListener {
    // declaring properties
    JFrame frame;

    JMenuBar menuBar;

    JMenu file,edit;

    JMenuItem newFile, openFile, saveFile;
    JMenuItem cut,copy,paste,selectAll,close;

    JTextArea textArea;

    TextEditor(){
        frame= new JFrame();

        //initialise the menu bar
        menuBar = new JMenuBar();

        //initalise the text area

        textArea = new JTextArea();

        //intitalise the "file" menu item
        newFile = new JMenuItem("New File");
        openFile = new JMenuItem("Open File");
        saveFile= new JMenuItem("Save File");

        //add actionlistenrs to file menu item
        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);

        //initialise the "edit" menu items
        cut= new JMenuItem("Cut");
        copy= new JMenuItem("Copy");
        paste= new JMenuItem("Paste");
        selectAll= new JMenuItem("Select All");
        close= new JMenuItem("Close");

        //add action listner to edit menu item
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        selectAll.addActionListener(this);
        close.addActionListener(this);

        //initalise the menus
        file = new JMenu("File");
        edit= new JMenu("Edit");

        //add scroll


        //add menu item to menu bar
        file.add(newFile);
        file.add(openFile);
        file.add(saveFile);

        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(selectAll);
        edit.add(close);

        //add menus to menu bar
        menuBar.add(file);
        menuBar.add(edit);

        //set the menu bar to frame
        frame.setJMenuBar(menuBar);

        //add text area
        //frame.add(textArea);

        //create content pane
        JPanel panel= new JPanel();
        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.setLayout(new BorderLayout(0,0));

        //add text area to panel
        panel.add(textArea, BorderLayout.CENTER);

        //create Scroll pane
        JScrollPane scrollPane = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane);

        //add panel to Frame
        frame.add(panel);

        //set dimension of frame;
        frame.setBounds(200,200,400,400);
        frame.setVisible(true);
        frame.setLayout(null);
    }

    public static void main(String[] args) {
        TextEditor textEditor= new TextEditor();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==openFile){
            JFileChooser fileChooser = new JFileChooser("D:");
            int chooseOption = fileChooser.showOpenDialog(null);
            //if we have clicked on open button
            if(chooseOption==JFileChooser.APPROVE_OPTION){
                //getting selected file
                File file= fileChooser.getSelectedFile();

                //get the path of selected file
                String filePath= file.getPath();
                try{
                    //initialise file reader
                    FileReader fileReader = new FileReader(filePath);
                    BufferedReader bufferedReader = new BufferedReader((fileReader));

                    String intermediate, output="";

                    //read content of file line by line
                    while((intermediate=bufferedReader.readLine())!=null){
                        output+=intermediate+"\n";
                    }

                    //set the output string to text Area

                    textArea.setText(output);
                }
                catch(FileNotFoundException fileNotFoundException){
                    fileNotFoundException.printStackTrace();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        if(e.getSource()==saveFile){
            //initialise the file picker
            JFileChooser fileChooser= new JFileChooser("D:");

            int chooseOption = fileChooser.showSaveDialog(null);

            //check if we clicked on save button
            if(chooseOption==JFileChooser.APPROVE_OPTION){
                //create a new file with choosen directory path and file name
                File file= new File(fileChooser.getSelectedFile().getAbsolutePath()+".txt");
                try{
                    FileWriter fileWriter = new FileWriter(file);
                    //initialise buffered writer
                    BufferedWriter bufferedWriter= new BufferedWriter(fileWriter);
                    // write contents of text area to file
                    textArea.write(bufferedWriter);
                    bufferedWriter.close();
                }
                catch(IOException ioException){
                    System.out.println(ioException);
                }

            }

        }

        if(e.getSource()==newFile){
            TextEditor new_texted = new TextEditor();
        }

        if(e.getSource()==cut){
            String selectedText = textArea.getSelectedText();



            if(selectedText!=null){
                //copy the selected text to clipboard
                StringSelection stringSelection = new StringSelection(selectedText);
//                Clipboard clipboard= new Clipboard("My clipboard");
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); // if we want to cut and paste anywhere in the system

                clipboard.setContents(stringSelection, null);

                textArea.replaceSelection("");

            }
        }

        if(e.getSource()==copy){
            String selectedText = textArea.getSelectedText();
            if(selectedText!=null){
                StringSelection stringSelection = new StringSelection(selectedText);

                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        }

        if(e.getSource()==paste){
            Clipboard clipboard= Toolkit.getDefaultToolkit().getSystemClipboard();

            //get the clipboard contents
            try{
                if(clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)){
                    String clipboardText = (String) clipboard.getData(DataFlavor.stringFlavor);
                    textArea.insert(clipboardText,textArea.getCaretPosition());
                }
            }
            catch(UnsupportedFlavorException | IOException ex){
                ex.printStackTrace();
            }
        }
        if(e.getSource()==selectAll){
            textArea.selectAll();
        }
        if(e.getSource()==close){
            System.exit(0);
        }
    }
}
