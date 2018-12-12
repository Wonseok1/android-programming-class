package raejin.listviewexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    // 어답터를 만드는데 필요한 맴버번수들,
    ArrayList<ItemDate> temp_list;  // 자료를 담고 있는 ArrayList 객체를 저장할 변수
    Context context;                // 엑티비티의 정보를 저장할 변수
    LayoutInflater layoutInflater;  // 아이템 레이아웃을 생성해주는 객체를 저장할 변수
    int item_layout;                // 디자인한 레이아웃의 아이디를 저장할 변수
    int item_layout2;

    MyAdapter(Context context, int item_layout, int item_layout2,
              ArrayList<ItemDate> temp_list) {
        this.context = context;             // 변수에 엑티비티의 정보를 저장
        this.item_layout = item_layout;     // 변수에 레이아웃의 아이디를 저장
        this.temp_list = temp_list;         // 변수에 자료를 담고 있는 ArrayList 객체를 저장
        this.layoutInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);   // 아이템 레이아웃을 생성해주는 객체를 만들고 저장

        this.item_layout2 = item_layout2;
    }

    /**
     * 항목에 나타낼 데이터의 갯수를 리턴하는 getCount()
     * @return ArrayList 객체에 담긴 데이터의 갯수
     */
    @Override
    public int getCount() { return temp_list.size(); }

    /**
     * 순서번호 i 번째의 데이터 객체를 리턴하는 getItem() 함수
     * @param i ArrayList에 저장된 데이터의 순서번호
     * @return i 번째의 데이터 객체
     */
    @Override
    public Object getItem(int i) { return temp_list.get(i); }

    /**
     * 순서번호 i 번째의 데이터 객체 순서를 리턴하는 getItemId() 함수
     * @param i ArrayList에 저장된 데이터의 순서번호
     * @return i 번째 데이터 객체 순서
     */
    @Override
    public long getItemId(int i) { return i; }

    /**
     * 항목을 만들어주는 getView() 함수
     * @param i 리스트 뷰안에 항목의 순서 번호, 데이터의 순서번호
     * @param view 항목 레이아웃 객체
     * @param viewGroup 리스트뷰 객체
     * @return 항목 레이아웃 객체
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;

        /*
            hint
         */

        if(view == null) {
            view = layoutInflater.inflate(item_layout, viewGroup, false);
        }

        if(i % 2 == 1) {
            view = layoutInflater.inflate(item_layout2, viewGroup, false);
            // 항목 레이아웃 안에 배치한 컴포넌트의 객체를 만듬
            ImageView iv = (ImageView)view.findViewById(R.id.imageView_item2_img);
            TextView tv = (TextView)view.findViewById(R.id.textView_item2_name);
            TextView tv_desc = (TextView)view.findViewById(R.id.textView_item2_desc);

            // 데이터를 저장한 arrayList에서 i번째 자료를 가져와서 컴포넌트 객체에 세팅한다.
            iv.setImageResource(temp_list.get(i).getImage_id());
            tv.setText(temp_list.get(i).getName());
            tv_desc.setText(temp_list.get(i).getDesc());

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,
                            temp_list.get(pos).getName() + "의 사진입니다.",
                            Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        } else {
            view = layoutInflater.inflate(item_layout, viewGroup, false);
            // 항목 레이아웃 안에 배치한 컴포넌트의 객체를 만듬
            ImageView iv = (ImageView)view.findViewById(R.id.imageView_item_img);
            TextView tv = (TextView)view.findViewById(R.id.textView_item_name);

            // 데이터를 저장한 arrayList에서 i번째 자료를 가져와서 컴포넌트 객체에 세팅한다.
            iv.setImageResource(temp_list.get(i).getImage_id());
            tv.setText(temp_list.get(i).getName());

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,
                            temp_list.get(pos).getName() + "의 사진입니다.",
                            Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }
        /*
        // 처음 getView()함수가 불려왔을 때는 항목 객체가 하나도 없는 상태이므로 항목 객체를 만들어준다.
        if(view == null) {
            view = layoutInflater.inflate(item_layout, viewGroup, false);
        }

        // 항목 레이아웃 안에 배치한 컴포넌트의 객체를 만듬
        ImageView iv = (ImageView)view.findViewById(R.id.imageView_item2_img);
        TextView tv = (TextView)view.findViewById(R.id.textView_item2_name);
        TextView tv_desc = (TextView)view.findViewById(R.id.textView_item2_desc);

        // 데이터를 저장한 arrayList에서 i번째 자료를 가져와서 컴포넌트 객체에 세팅한다.
        iv.setImageResource(temp_list.get(i).getImage_id());
        tv.setText(temp_list.get(i).getName());
        tv_desc.setText(temp_list.get(i).getDesc());

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,
                        temp_list.get(pos).getName() + "의 사진입니다.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
        */
    }


}
