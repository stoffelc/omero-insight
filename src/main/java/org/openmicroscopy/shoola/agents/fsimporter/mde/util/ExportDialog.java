/*
 * Copyright (C) <2016-2019> University of Dundee & Open Microscopy Environment.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openmicroscopy.shoola.agents.fsimporter.mde.util;

import org.openmicroscopy.shoola.agents.fsimporter.mde.util.inout.TypeFilter_GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * Dialog to export form input to file.
 * The dialog allows you to specify the target directory and file name of export file as well as configurations
 * regarding the structure of the key for the key-value pairs.
 *
 * 7/2/2020
 * @author Susanne Kunis<susannekunis at gmail dot com>
 **/
public class ExportDialog extends JDialog implements ActionListener {

    private JButton btn_OK;
    private JButton btn_cancel;
    private JButton btn_browse_save;
    private JTextField txt_path;
    private JCheckBox ch_addPath;
    private JCheckBox ch_addUnitToKey;
    private JCheckBox ch_exportAllData;

    private final String suffix=".csv";
    private DefaultMutableTreeNode tree;
    private boolean cancel;

    private File exportfile;

    public ExportDialog(JFrame parent){
        super(parent,"Export to csv file");
        this.exportfile=null;
        cancel=false;
        buildGUI();
        pack();
        setVisible(true);
    }

    private void buildGUI() {
        setBounds(100, 100, 500, 600);
        getContentPane().setLayout(new BorderLayout(5,5));
        setModal(true);

        btn_OK = new JButton("OK");
        btn_OK.addActionListener(this);
        btn_cancel = new JButton("Cancel");
        btn_cancel.addActionListener(this);
        Box btnPane=Box.createHorizontalBox();
        btnPane.add(btn_cancel);
        btnPane.add(Box.createHorizontalStrut(5));
        btnPane.add(btn_OK);


        /** configuration panel **/
        JPanel subPanel= new JPanel();
        Border titleBorder = BorderFactory.createTitledBorder("Configuration:");
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
        subPanel.setBorder(titleBorder);
        ch_addPath = new JCheckBox("use tree path as addition to key");
        ch_addPath.setToolTipText("Set pattern of key: <objectName>#..#<objectName>|<propertyName>");
        ch_addPath.setSelected(true);
        subPanel.add(ch_addPath);
        ch_addUnitToKey=new JCheckBox("add unit to key");
        ch_addUnitToKey.setSelected(false);
        subPanel.add(ch_addUnitToKey);
        ch_exportAllData=new JCheckBox("Export all metadata");
        ch_exportAllData.setSelected(false);
        ch_exportAllData.setToolTipText("Export also metadata that available in the image container");
        subPanel.add(ch_exportAllData);

        /** main panel**/
        JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new BorderLayout(5,5));
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainPanel.add(subPanel,BorderLayout.CENTER);


        JLabel destPath_Lbl=new JLabel("Destination");
        txt_path =new JTextField(50);
        txt_path.setEditable(false);
        txt_path.setToolTipText("Destination to store csv file");
        if(exportfile!=null)
            txt_path.setText(exportfile.getAbsolutePath());
        btn_browse_save =new JButton("Browse");
        btn_browse_save.addActionListener(this);
        JPanel destP=new JPanel();
        destP.add(destPath_Lbl);
        destP.add(txt_path);
        destP.add(btn_browse_save);

        mainPanel.add(destP,BorderLayout.SOUTH);

        getContentPane().add(mainPanel,BorderLayout.CENTER);
        getContentPane().add(btnPane,BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btn_OK){
            setVisible(false);
            dispose();
        }else if(e.getSource()== btn_cancel) {
            cancel =true;
            setVisible(false);
            dispose();
        }else if(e.getSource()== btn_browse_save) {
            FileFilter filter = new FileNameExtensionFilter("CSV file", "csv");
            JFileChooser fcSave =new JFileChooser();
            fcSave.addChoosableFileFilter(filter);
            fcSave.setFileFilter(filter);
            if(exportfile!=null)
                fcSave.setCurrentDirectory(new File(exportfile.getParent()));
            int returnValSave=fcSave.showSaveDialog(this);
            if(returnValSave==JFileChooser.APPROVE_OPTION) {
                exportfile=fcSave.getSelectedFile();
                if(!fcSave.getSelectedFile().getAbsolutePath().endsWith(suffix)){
                    exportfile = new File(fcSave.getSelectedFile() + suffix);
                }
                txt_path.setText(exportfile.getAbsolutePath());
            }
        }
    }
    public File getDestination()
    {
        return exportfile;
    }

    public boolean addPath() {
        return ch_addPath.isSelected();
    }
    public boolean addUnitToKey(){
        return ch_addUnitToKey.isSelected();
    }
    public boolean exportAll(){
        return ch_exportAllData.isSelected();
    }
    public Boolean isCancelled(){return cancel;}
}
