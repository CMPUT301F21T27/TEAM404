package com.example.team404;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * This class is an adapter class
 */
public class Content extends ArrayAdapter<Habit> {
private ArrayList<Habit> habits;
private Context context;

    /**
     * constructor
     * @param c
     * @param h
     */
    public Content(Context c, ArrayList<Habit> h){
    super(c,0,h);
    this.habits =h;
    this.context=c;


}

    /**
     * Connects to the layout and format the habits as item in the list
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent){
    View view = convertView;
    if(view == null){
        view = LayoutInflater.from(context).inflate((R.layout.habit_content),parent,false);

    }
    Habit habit = habits.get(position);

    TextView habitTitle = view.findViewById(R.id.titleTextView);
    TextView habitReason = view.findViewById(R.id.reasonTextView);
    TextView habitDate = view.findViewById(R.id.dateTextView);


    habitTitle.setText(habit.getTitle());
    habitDate.setText(habit.getYear() + "-" +habit.getMonth()+ "-" +habit.getDay());
    habitReason.setText(habit.getReason());


    return view;

}
}
