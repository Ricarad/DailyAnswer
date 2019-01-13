package com.ricarad.app.dailyanswer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.activity.AddTopicActivity;
import com.ricarad.app.dailyanswer.model.User;

public class DiscussFragment extends Fragment implements View.OnClickListener{
    private LinearLayout ll_add; //发帖"按钮"

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discuss_fragment_guide,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ll_add = getActivity().findViewById(R.id.discuss_add_ll);

        ll_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.discuss_add_ll:
                goToAddTopic();
                break;
        }
    }

    private void goToAddTopic(){
        Intent intent = new Intent(getContext(), AddTopicActivity.class);

        if (isAdded()) {  //判断Fragment已经依附Activity
            User user = (User)getArguments().getSerializable("user");
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }
}
