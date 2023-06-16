package com.desay.sport.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.desay.sport.main.R;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

/**
 * 
 * @author wzq
 * �Զ���Ŀ��������б��������<br />
 * �����´�����ʾ��ʹ��ʱ��A,B�����б����һ���б���group1�£�
 * <code>
 * String[] groups={"group1", "group2", "group3"};
 * String[][] childs={{"A", "B"},{"C", "D"},{"E", "F"}};
 * provinceList= (ExpandableListView)findViewById(R.id.provinceList);
 * BaseExpandableListAdapter adapter=new MyListAdapter(this, groups, childs);
 * provinceList.setAdapter(adapter);
 * </code>
 * ʹ��ʱע������б��Ķ�Ӧ��ϵ
 */
public class cityAdapter extends BaseExpandableListAdapter implements Filterable {
	
	/**
	 * �����һ���˵�������
	 */
	private String[] groups;
	/**
	 * ����Ķ����˵�������
	 */
	private String[][] childs;
	
	/**
	 * ȫ����һ���˵�������
	 */
	private String[] allGroups;
	
	/**
	 * ȫ���Ķ����˵�������
	 */
	private String[][] allChilds;
	
	/**
	 * ��¼������Context�����ڲ���һ��TextViewʵ��
	 * @see #getGenericView
	 */
	private Context context;
	
	/**
	 * �Զ���ĳ��й�����
	 */
	private CityFilter filter;
	
	/**
	 * ��¼��ǰʹ�ô����������������б�
	 */
	private ExpandableListView provinceList;
	
	/**
	 * ���췽����ָ��һ���˵������˵��������һ�����������б��������
	 * ���е�childsҪ��groups������ֵ��Ӧ
	 * <br />
	 * @param context Ϊ <code>Context</code>�������ڼ�¼�˿ؼ��������ı�����
	 * @param groups Ϊ<code>String</code>�������飬�����һ���˵�������
	 * @param childs Ϊ<code>String</code>��������,��groups����ֵ���Ӧ�Ķ����˵����ά����
	 */
	public cityAdapter(Context context, ExpandableListView listView, String[] groups, String[][] childs) {
		this.context = context;
		this.groups = groups;
		this.childs = childs;
		allGroups = groups;
		allChilds = childs;
		this.provinceList = listView;
	}
	
	
	/**
	 * ��һ���˵�������ֵ<code>groupPosition</code>������˵�������ֵ<code>childPosition</code>
	 * ������ָ���Ķ���ֵ<br />
	 * @param groupPostionΪһ���˵��������
	 * @param childPostionΪ��Ӧ�Ķ����˵�����
	 * @return �����˵��������
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childs[groupPosition][childPosition];
	}

	/**
	 * ��һ���˵�������ֵ<code>groupPosition</code>������˵�������ֵ<code>childPosition</code>
	 * �����������ID
	 * @param groupPostionΪһ���˵��������
	 * @param childPostionΪ��Ӧ�Ķ����˵�����
	 * @return �����Ӳ˵����ID
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	/**
	 * @return �������б����<code>View<code>����
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		TextView textView = null;
		//�����ж�convertView�Ƿ�Ϊ�գ���Ϊ����ĸ��ã��������
		if(convertView==null) {
			//����һ��TextView���
			textView = getGenericView();
			//��������ı�����
			textView.setText(getChild(groupPosition, childPosition).toString());
		} else {
			textView = (TextView)convertView;
			textView.setText(getChild(groupPosition, childPosition).toString());
		}
		
		return textView;
	}

	/**
	 * �õ�Ӧһ���б�������б������
	 * @param groupPositionһ���б������ֵ
	 * @return ��Ӧ���鼶�б����
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return childs[groupPosition].length;
	}

	/**
	 * ��һ���б�����ֵ<code>groupPosition</code>�õ�һ���б���������
	 * @return һ���б�������
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return groups[groupPosition];
	}

	/**
	 * һ���б�ĸ���
	 * @return ����һ���б����
	 */
	@Override
	public int getGroupCount() {
		return groups.length;
	}

	/**
	 * �õ�һ���б�ID,������ֵ
	 * @param groupPositionΪһ���б������ֵ
	 * @return һ���б�ID����<code>0</code>��ʼ
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * @return һ���б��Ӧ��<code>View</code>����ʵ��
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView textView = null;
		if(convertView==null){
			textView = getGenericView();
			textView.setText(getGroup(groupPosition).toString());
		}else {
			textView = (TextView)convertView;
			textView.setText(getGroup(groupPosition).toString());
		}
		
		return textView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	/**
	 * �ж�ָ���Ķ���ѡ���Ƿ��ѡ
	 * @param groupPosition һ���б�������ֵ
	 * @param childPosition �����б�������ֵ
	 * @return ������
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	/**
	 * ����һ��TextView����ʵ��
	 * @return һ��TextView���ʵ��
	 */
	 private TextView getGenericView() {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,dip2px(45));
		
		TextView textView = new TextView(context);
		
		textView.setLayoutParams(lp);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.fontsize_36));
		textView.setTextColor(Color.BLACK);
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textView.setBackgroundResource(R.drawable.bg_list);
		textView.setPadding(dip2px(35), 0, 0, 0);
		return textView;
	}

	/**
	 * ���ش��������б�Ĺ�����
	 * @return Filter һ������������
	 */
	@Override
	public Filter getFilter() {
		if(filter == null) {
			filter = new CityFilter();
		}
		return filter;
	}
	
	private class CityFilter extends Filter {

		/**
		 * �������ַ��������б?���ع��˺�Ľ���װ����FilterResults
		 * @param constraint ���˵������ַ�
		 * @return һ����װ���˵Ľ����
		 */
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			
			//���ڴ�ŷ��������ʡ������ֵ,����֮��Ӧƥ��ĳ���
			Map<Integer, ArrayList<Integer>> values = new HashMap<Integer, ArrayList<Integer>>();
			//����������Ϊ��ʱ���������е�ʡ�������
			if(constraint == null || constraint.length() == 0) {
				for(int i=0; i<allGroups.length; i++) {
					ArrayList<Integer> index = new ArrayList<Integer>();
					//���������֮��Ӧ�ĳ���
					for(int j=0; j<allChilds[i].length; j++) {
						index.add(j);
					}
					values.put(i, index);
				}
			} else {
				String filterStr = constraint.toString();
				for(int i=0; i<allGroups.length; i++) {
					//����ʡ���Ƿ���û�������ַ�
					if(allGroups[i].contains(filterStr)) {
						ArrayList<Integer> index = new ArrayList<Integer>();
						//���������֮��Ӧ�ĳ���
						for(int j=0; j<allChilds[i].length; j++) {
							index.add(j);
						}
						values.put(i, index);
					} else {
						ArrayList<Integer> index = new ArrayList<Integer>();
						//���ʡ����û�У������������ĳ������Ƿ��
						for(int j=0; j<allChilds[i].length; j++) {
							if(allChilds[i][j].contains(filterStr)) {
								index.add(j);
							}
						}
						//�����ӽ����˳��У�˵�����ڣ������ʡ��Ҳ��ӽ�ȥ
						if(index.size() > 0) {
							values.put(i, index);
						} else {
							index = null;
						}
					}
				}
			}
			
			results.values = values;
			results.count = values.size();
			
			return results;
		}

		/**
		 * �������˵õ��Ľ��results���������������б����
		 * @param constraint ��������
		 * @param results ���˵õ��Ľ��
		 */
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			//�õ����˺��ʡ�������б�
			Map<Integer, ArrayList<Integer>> filterResult = (Map<Integer, ArrayList<Integer>>) results.values;
			int count = filterResult.size();
			//���ֵ����
			if( count > 0) {
				String[] newGroups = new String[count];
				String[][] newChilds = new String[count][];
				int index = 0;
				int length = 0;
				//�õ��µ�groups��childs
				for(int i=0; i<allGroups.length; i++) {
					if(filterResult.containsKey(i)) {
						newGroups[index] = allGroups[i];
						//��������ĳ���
						ArrayList<Integer> citys = filterResult.get(i);
						length = citys.size();
						newChilds[index] = new String[length];
						for(int j = 0; j< length; j++) {
							newChilds[index][j] = allChilds[i][citys.get(j)];
						}
						index = index + 1;
					}
				}
				//����groups��childs
				groups = newGroups;
				childs = newChilds;
				
				//�����б�
				notifyDataSetChanged();
				
				//�ж��Ƿ�չ���б�
				count = getGroupCount();
				if(count < 34) {
					//չ���������б�
					for(int i=0; i<count; i++) {
						provinceList.expandGroup(i);
					}
				} else {
					//�����������б�
					for(int i=0; i<count; i++) {
						provinceList.collapseGroup(i);
					}
				}
			} else {
				//û�й���ֵ����֪ͨΪ��Ч����ݸ���
				notifyDataSetInvalidated();
			}
		}
	}
	public int dip2px(float dpValue) { 
		final float scale = context.getResources().getDisplayMetrics().density;  
		return (int) (dpValue * scale + 0.5f);  } 
}
