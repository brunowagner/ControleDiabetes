package br.com.bwsystemssolutions.controlediabetes.androidFileAndDirectoryPicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.bwsystemssolutions.controlediabetes.R;

/**
 * Adaptador para testar o Custom file explorer
 * fonte: https://www.technetexperts.com/mobile/custom-file-explorer-in-android-application-development/
 */
public class MyListAdapter extends BaseAdapter {
    private List<String> m_item;
    private List<String> m_path;
    public ArrayList<Integer> m_selectedItem;
    Context m_context;
    Boolean m_isRoot;

    private int m_itemBackgroundColor;
    private int m_selectedItemBackgroundColor;



    public MyListAdapter(Context p_context, List<String> p_item, List<String> p_path, Boolean p_isRoot) {
        m_context=p_context;
        m_item=p_item;
        m_path=p_path;
        m_selectedItem=new ArrayList<Integer>();
        m_isRoot=p_isRoot;
    }

    @Override
    public int getCount() {
        return m_item.size();
    }

    @Override
    public Object getItem(int position) {
        return m_item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int p_position, View p_convertView, ViewGroup p_parent)
    {

        View m_view = null;
        ViewHolder m_viewHolder = null;
        if (p_convertView == null)
        {
            LayoutInflater m_inflater = LayoutInflater.from(m_context);
            //m_view = m_inflater.inflate(R.layout.list_row, null);
            m_view = m_inflater.inflate(R.layout.list_item_row_afadp, null);
            m_viewHolder = new ViewHolder();
            m_viewHolder.m_tvFileName = (TextView) m_view.findViewById(R.id.lr_tvFileName);
            m_viewHolder.m_tvDate = (TextView) m_view.findViewById(R.id.lr_tvdate);
            m_viewHolder.m_ivIcon = (ImageView) m_view.findViewById(R.id.lr_ivFileIcon);
            m_view.setTag(m_viewHolder);
        }
        else
        {
            m_view = p_convertView;
            m_viewHolder = ((ViewHolder) m_view.getTag());
        }
        if(!m_isRoot && p_position == 0)
        {
            //m_viewHolder.m_cbCheck.setVisibility(View.INVISIBLE);  was used when there was checkbox
        }

        m_viewHolder.m_tvFileName.setText(m_item.get(p_position));
        m_viewHolder.m_ivIcon.setImageResource(setFileImageType(new File(m_path.get(p_position))));
        m_viewHolder.m_tvDate.setText(getLastDate(p_position));

        if (m_selectedItem.contains(p_position)){
            m_view.setBackgroundColor(m_selectedItemBackgroundColor);
        } else {
            m_view.setBackgroundColor(m_itemBackgroundColor);
        }

        return m_view;
    }

    class ViewHolder
    {
        ImageView m_ivIcon;
        TextView m_tvFileName;
        TextView m_tvDate;
    }

    private int setFileImageType(File m_file)
    {
        int m_lastIndex=m_file.getAbsolutePath().lastIndexOf(".");
        String m_filepath=m_file.getAbsolutePath();
        if (m_file.isDirectory())
            return R.drawable.ic_icons8_folder_48px_afadp;
        else
        {
            if(m_filepath.substring(m_lastIndex).equalsIgnoreCase(".png"))
            {
                return R.drawable.ic_icons8_file_48px_afadp;
            }
            else if(m_filepath.substring(m_lastIndex).equalsIgnoreCase(".jpg"))
            {
                return R.drawable.ic_icons8_file_48px_afadp;
            }
            else
            {
                return R.drawable.ic_icons8_file_48px_afadp;
            }
        }
    }

    String getLastDate(int p_pos)
    {
        File m_file=new File(m_path.get(p_pos));
        SimpleDateFormat m_dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return m_dateFormat.format(m_file.lastModified());
    }

    public void setItemBackgroundColor(int defaultColor, int selectedColor) {
        this.m_itemBackgroundColor = defaultColor;
        this.m_selectedItemBackgroundColor = selectedColor;
    }
}
