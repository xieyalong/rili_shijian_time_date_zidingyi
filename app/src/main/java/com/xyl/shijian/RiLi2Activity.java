package com.xyl.shijian;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class RiLi2Activity extends AppCompatActivity {
    Context mContext;
    int index=0;
    int year=2021;
    int month=1;
    int yearSize=2;//当前时间的前几年后几年
    ViewPager2 vp_page;
    TextView tv_n;
    List<String> data=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rili2);
        mContext=this;
        defaultDate();
        try {
            tv_n=findViewById(R.id.tv_n);
            tv_n.setText(year+"-"+month);
            initViewPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void  defaultDate(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        for (int i =year-yearSize; i <=year+yearSize ; i++) {
            for (int j = 1; j <13 ; j++) {
                data.add(i+"-"+j);
            }
        }
        String ym=year+"-"+month;
        for (int i = 0; i <data.size() ; i++) {
            if (data.get(i).equals(ym)){
                index=i;
                break;
            }
        }
    }

    public  void initViewPage(){
        vp_page=findViewById(R.id.vp_page);
//            vp_page.setOffscreenPageLimit(0);
        vp_page.setAdapter(new MyViewPagerAdapter());
        vp_page.setCurrentItem(index);
        vp_page.registerOnPageChangeCallback(new OnPageChangeCallback() {
            //记录上一次滑动的positionOffsetPixels值
            private int lastValue = -1;
            private boolean isLeft = true;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffset != 0) {
                    if (lastValue >= positionOffsetPixels) {
                        //右滑
                        isLeft = false;
                    } else if (lastValue < positionOffsetPixels) {
                        //左滑
                        isLeft = true;
                    }
                }
                lastValue = positionOffsetPixels;
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (isLeft){
                    System.out.println("--->]左划 year="+year+",month="+month);
                }else {
                    System.out.println("--->]右划 year="+year+",month="+month);
                }
                tv_n.setText(data.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    public class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder>{
        public MyAdapter() {
            super(R.layout.view_rili_item);
        }

        @Override
        protected void convert(BaseViewHolder helper, final String item) {
            try {
                helper.setText(R.id.tv_txt,item.split("-")[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class MyViewPagerAdapter extends RecyclerView.Adapter<MyViewPagerAdapter.ViewPagerViewHolder> {

        public MyViewPagerAdapter() {}


        @NonNull
        @Override
        public ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewPagerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rili, parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewPagerViewHolder holder, int position) {
            String[] arr=data.get(position).split("-");
            year=Integer.parseInt(arr[0]);
            month=Integer.parseInt(arr[1]);


            GridLayoutManager layoutManager=new GridLayoutManager(mContext,7);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            holder.rv_list.setLayoutManager(layoutManager);

            MyAdapter myAdapter=new MyAdapter();
            myAdapter.setNewData(getDaysByYearMonth(year,month));
            holder.rv_list.setAdapter(myAdapter);
            System.out.println(">]aaa="+position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
        class ViewPagerViewHolder extends RecyclerView.ViewHolder {
            RecyclerView rv_list;
            public ViewPagerViewHolder(@NonNull View itemView) {
                super(itemView);
                rv_list = itemView.findViewById(R.id.rv_list);
            }
        }

    }





    public  List<String>  getDaysByYearMonth(int year, int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        int maxDate = calendar.get(Calendar.DATE);
        System.out.println(">]maxDate="+maxDate);
        List<String> list=new ArrayList<>();
        for (int i = 0; i <maxDate ; i++) {
            list.add(year+"-"+month+"-"+(i+1));
        }



        calendar.set(year,month-1,1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String currentDate = sdf.format(calendar.getTime());
        System.out.println(">]date="+currentDate);

        int week = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println(">]week="+week);
//        int weekStr = 0;
        int  weekIndex=0;
        /*星期日:Calendar.SUNDAY=1
         *星期一:Calendar.MONDAY=2
         *星期二:Calendar.TUESDAY=3
         *星期三:Calendar.WEDNESDAY=4
         *星期四:Calendar.THURSDAY=5
         *星期五:Calendar.FRIDAY=6
         *星期六:Calendar.SATURDAY=7 */
        switch (week) {
            case 1:
//                weekStr = 7;
                weekIndex=0;
                break;
            case 2:
//                weekStr = 1;
                weekIndex=1;
                break;
            case 3:
                weekIndex=2;
//                weekStr = 2;
                break;
            case 4:
                weekIndex=3;
//                weekStr = 3;
                break;
            case 5:
                weekIndex=4;
//                weekStr = 4;
                break;
            case 6:
                weekIndex=5;
//                weekStr = 5;
                break;
            case 7:
                weekIndex=6;
//                weekStr = 6;
                break;
            default:
                break;
        }
        System.out.println(">]weekIndex="+weekIndex);
        if (weekIndex>0){
            Calendar calendar2;
            List<String> list1=new ArrayList<>();
            for (int i = 0; i <weekIndex ; i++) {
                calendar2= Calendar.getInstance(); //得到日历
                calendar2.setTime(calendar.getTime());//把当前时间赋给日历
                calendar2.add(Calendar.DAY_OF_MONTH, -(i+1));  //设置为前一天
                String time=sdf.format(calendar2.getTime());
                list1.add(time);
            }
            Collections.reverse(list1); // 倒序排列
            for (int i = 0; i <list1.size() ; i++) {
                list.add(i,list1.get(i));
            }
        }
        return list;
    }


}
