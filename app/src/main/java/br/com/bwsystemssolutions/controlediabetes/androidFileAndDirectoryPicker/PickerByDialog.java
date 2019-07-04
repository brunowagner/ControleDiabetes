package br.com.bwsystemssolutions.controlediabetes.androidFileAndDirectoryPicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import br.com.bwsystemssolutions.controlediabetes.R;
import br.com.bwsystemssolutions.controlediabetes.androidFileAndDirectoryPicker.adapter.MyListAdapter;

public class PickerByDialog implements DialogInterface.OnClickListener, DialogInterface.OnShowListener {

    private Context mContext;
    private int mthemeResId;

    public static final String TITLE_STRING_NAME = "title";
    public static final int SELECT_TYPE_FOLDER = 0;
    public static final int SELECT_TYPE_FILE = 1;
    public static final int SELECT_TYPE_ANY = 2;

    private ArrayList<String> m_item;
    private ArrayList<String> m_path;
    private ArrayList<String> m_files;
    private ArrayList<String> m_filesPath;
    private String m_curDir;
    private MyListAdapter m_listAdapter;
    private ListView m_RootList;
    private String m_root = "";
    private Boolean m_isRoot=true;

    private String m_title = "Select Folder";
    private String m_localLabel = "Location:";
    private int m_itemBackgroundColor;
    private int m_selectedItemBackgroundColor;
    private OnResponseListener m_onResponseListener;
    private int m_selectType = SELECT_TYPE_FOLDER;
    private String m_selectButtonTitle = "Select";
    private String m_newFolderButtonTitle = "New Folder";
    private String m_cancelButtonTitle = "Cancel";
    private String m_inputNewFolderTitle = "New folder name:";
    private TextView m_curDirTextView;
    private TextView m_localTextView;
    private Button m_selectButton;


    public PickerByDialog(Context context, String root){
        mContext = context;
        m_root = root;
    }

    public PickerByDialog(Context context, String root, int themeResId){
        mContext = context;
        m_root = root;
        mthemeResId = themeResId;
    }

    public void setTitle(String title){
        m_title = title;
    }

    public void setLocalLabel(String localLabel){
        m_localLabel = localLabel;
    }

    public void setSelectButtonTitle(String selectButtonTitle) {
        this.m_selectButtonTitle = selectButtonTitle;
    }

    public void setNewFolderButtonTitle(String newFolderButtonTitle) {
        this.m_newFolderButtonTitle = newFolderButtonTitle;
    }

    public void setCancelButtonTitle(String cancelButtonTitle) {
        this.m_cancelButtonTitle = cancelButtonTitle;
    }

    public void setInputNewFolderTitle(String inputNewFolderTitle) {
        this.m_inputNewFolderTitle = inputNewFolderTitle;
    }

    public void show(){

        AlertDialog.Builder dialogBuilder;

        dialogBuilder = mthemeResId > 0 ? new AlertDialog.Builder(mContext, mthemeResId) : new AlertDialog.Builder(mContext);

        dialogBuilder.setPositiveButton(m_selectButtonTitle, this);
        dialogBuilder.setNegativeButton(m_cancelButtonTitle, this);
        //dialogBuilder.setNeutralButton("Nova Pasta", this);
        dialogBuilder.setNeutralButton(m_newFolderButtonTitle, null);

        if (m_title.length()>0) dialogBuilder.setTitle(m_title);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.list_row_afadp, null);

//        if (m_localLabel.length()>0) {
//            TextView textView = view.findViewById(R.id.tv_title);
//            textView.setVisibility(View.VISIBLE);
//            textView.setText(m_localLabel);
//        }

        m_localTextView = view.findViewById(R.id.tv_local);
        m_localTextView.setText(m_localLabel);

        m_curDirTextView = view.findViewById(R.id.tv_title);
        m_curDirTextView.setVisibility(View.VISIBLE);

        // this force scrollbar go to full right
        m_curDirTextView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ((HorizontalScrollView) v.getParent()).fullScroll(View.FOCUS_RIGHT);
            }
        });


        m_listAdapter=new MyListAdapter(mContext,m_item,m_path, m_isRoot);

        m_RootList = view.findViewById(R.id.lv_lvListRoot);

        getDirFromRoot(m_root);

        dialogBuilder.setView(view);

        AlertDialog dialog = dialogBuilder.create();
        dialog.setOnShowListener(this);

        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }



    //get directories and files from selected path
    private void getDirFromRoot(String p_rootPath)
    {
        m_item = new ArrayList<String>();

        m_path = new ArrayList<String>();
        m_files = new ArrayList<String>();
        m_filesPath=new ArrayList<String>();
        File m_file = new File(p_rootPath);
        File[] m_filesArray = m_file.listFiles();
        if(!p_rootPath.equals(m_root))
        {
            m_item.add("../");
            m_path.add(m_file.getParent());
            m_isRoot=false;
        }
        m_curDirTextView.setText(m_file.getAbsolutePath());

        m_curDir=p_rootPath;

        //sorting file list in alphabetical order
        if (m_filesArray != null) {
            Arrays.sort(m_filesArray);

            for (int i = 0; i < m_filesArray.length; i++) {
                File file = m_filesArray[i];
                if (file.isDirectory()) {
                    m_item.add(file.getName());
                    m_path.add(file.getPath());
                } else {
                    if (m_selectType == SELECT_TYPE_ANY || m_selectType == SELECT_TYPE_FILE) {
                        m_files.add(file.getName());
                        m_filesPath.add(file.getPath());
                    }
                }
            }
        }
        for(String m_AddFile:m_files)
        {
            m_item.add(m_AddFile);
        }
        for(String m_AddPath:m_filesPath)
        {
            m_path.add(m_AddPath);
        }
        configureAdapter();
    }

    private void configureAdapter (){
        m_listAdapter=new MyListAdapter(mContext,m_item,m_path,m_isRoot);
        m_listAdapter.setItemBackgroundColor(m_itemBackgroundColor, m_selectedItemBackgroundColor);
        m_RootList.setAdapter(m_listAdapter);

        m_RootList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                File m_isFile=new File(m_path.get(position));
                if(m_isFile.isDirectory())
                {
                    getDirFromRoot(m_isFile.toString());
                }
                else
                {
                    Toast.makeText(mContext, "This is File", Toast.LENGTH_SHORT).show();
                    if (m_listAdapter.m_selectedItem.contains(position) ) {
                        m_listAdapter.m_selectedItem.clear();
                        if (m_selectType == SELECT_TYPE_FILE) {
                            m_selectButton.setEnabled(false);
                        }
                    } else {
                        m_listAdapter.m_selectedItem.clear();
                        m_listAdapter.m_selectedItem.add(0, position);
                        if (m_selectType == SELECT_TYPE_FILE) {
                            m_selectButton.setEnabled(true);
                        }
                    }
                    m_listAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        int pressedButtom = which;

        switch (pressedButtom) {
            case DialogInterface.BUTTON_POSITIVE:
                select();
                dialog.cancel();
                dialog.dismiss();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                cancel();
                dialog.cancel();
                dialog.dismiss();

            default:break;
        }
    }

    private void select(){
        if (m_onResponseListener == null) return;
        String response = "";
        switch (m_selectType){
            case SELECT_TYPE_FOLDER:
                response = m_curDir;
                m_onResponseListener.onResponse(false, response);
                break;

            case SELECT_TYPE_FILE:
                if (m_listAdapter.m_selectedItem.size() == 0) {
                    m_onResponseListener.onResponse(false, response);
                    return;
                }
                response = m_path.get(m_listAdapter.m_selectedItem.get(0));
                m_onResponseListener.onResponse(false, response);
                break;


            case SELECT_TYPE_ANY:
                if (m_listAdapter.m_selectedItem.size() == 0) {
                    m_onResponseListener.onResponse(false, m_curDir);
                } else {
                    response = m_path.get(m_listAdapter.m_selectedItem.get(0));
                    m_onResponseListener.onResponse(false, response);
                }
                break;

                default:
        }

    }

    private void cancel(){
        if (m_onResponseListener == null) return;
        m_onResponseListener.onResponse(true, "");
    }

    private void newFolder(){
        Toast.makeText(mContext,"Botão de 'Nova Pasta' foi clicado!", Toast.LENGTH_SHORT).show();
        createNewFolder();
    }


    private void createNewFile (){
        create(0);
    }

    private void createNewFolder (){
        create(1);
    }

    private void create( final int p_opt)
    {
        Log.d("bwvm", "create: inicio do on create");
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(m_inputNewFolderTitle);

        // Set up the input
        final EditText m_edtinput = new EditText(mContext);
        // Specify the type of input expected;
        m_edtinput.setInputType(InputType.TYPE_CLASS_TEXT);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_text = m_edtinput.getText().toString();
                if(p_opt == 1)
                {
                    File m_newPath=new File(m_curDir,m_text);
                    if(!m_newPath.exists()) {
                        m_newPath.mkdirs();
                    }
                }
                else
                {
                    try {
                        FileOutputStream m_Output = new FileOutputStream((m_curDir+File.separator+m_text), false);
                        m_Output.close();
                        //  <!--<intent-filter>
                        //  <action android:name="android.intent.action.SEARCH" />
                        //  </intent-filter>
                        //  <meta-data android:name="android.app.searchable"
                        //  android:resource="@xml/searchable"/>-->

                    } catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                getDirFromRoot(m_curDir);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setView(m_edtinput);
        builder.show();
    }




    public void setItemBackgroundColor(int defaultColor, int selectedColor) {
        this.m_itemBackgroundColor = defaultColor;
        this.m_selectedItemBackgroundColor = selectedColor;
    }

    public void setOnResponseListener(OnResponseListener onResponseListener){
        this.m_onResponseListener = onResponseListener;
    }

    public void setSelectType(int pickerSelectType){
        this.m_selectType = pickerSelectType;
    }

    @Override
    public void onShow(DialogInterface dialog) {

        AlertDialog alertDialog = (AlertDialog) dialog;

        Button button = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFolder();
            }
        });

        m_selectButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

        if (m_selectType == SELECT_TYPE_FILE){
            m_selectButton.setEnabled(false);
        } else {
            m_selectButton.setEnabled(true);
        }
    }

    public interface OnResponseListener {
        void onResponse(boolean canceled, String response);
    }
}
