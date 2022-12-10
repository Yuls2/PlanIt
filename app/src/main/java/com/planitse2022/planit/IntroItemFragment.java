package com.planitse2022.planit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntroItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroItemFragment extends Fragment {
    private static final String[] LIST_TITLE = {"PLANiT에 오신 것을 환영합니다!"
            , "플래닛에 가입하세요"
            , "당신의 목표를 심으세요"
            , "매일 인증샷을 올리세요"
            , "이제 떠나볼까요?"};
    private static final String[] LIST_CONTENT = {"PLANiT은 동기 부여 체크리스트 앱입니다"
            , "PLANiT에는 같은 분야의 목표를 가진 사람들이 모인\n다양한 플래닛이 있습니다!"
            , "플래닛에 가입하면 목표 체크리스트를 작성할 수 있습니다\n그리고 그 목표는 식물이 되어 자랍니다!"
            , "오늘 한 일에 대한 인증샷을 올리면\n플래닛 멤버들이 나의 식물에게 물을 줄 수 있습니다"
            , "자, 그럼 이제 원하는 플래닛을 찾아 가입해보세요!"};
    private static final int[] LIST_IMAGE = {R.drawable.intro_0, R.drawable.intro_1, R.drawable.intro_2, R.drawable.intro_3, R.drawable.intro_4};
    private int position;

    public static IntroItemFragment newInstance(int position) {
        IntroItemFragment fragment = new IntroItemFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt("pos");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvContent = view.findViewById(R.id.tv_content);
        ImageView imgIntro = view.findViewById(R.id.img_intro);

        tvTitle.setText(LIST_TITLE[position]);
        tvContent.setText(LIST_CONTENT[position]);
        imgIntro.setImageResource(LIST_IMAGE[position]);
    }
}