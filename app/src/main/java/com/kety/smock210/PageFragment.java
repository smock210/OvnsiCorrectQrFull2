package com.kety.smock210.ovnsicorrectqrfull;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.kety.smock210.ovnsicorrectqrfull.adapter.TegAdapter;

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String QR_TEXT = "QR_TEXT";

    private int mPage;
    private String  mQrText;
    TegAdapter tegAdapter;

    public static PageFragment newInstance(int page, String qrText) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(QR_TEXT, qrText);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
            mQrText = getArguments().getString(QR_TEXT);
        }
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view;
        if (mPage==1) {

            view = inflater.inflate(R.layout.fragment_page, container, false);
            tegAdapter = new TegAdapter(view.getContext(), null);
            TextView vie = view.findViewById(R.id.fullCodeText);
            vie.setText(mQrText);
            ListView qrResult = (ListView) view.findViewById((R.id.construct));
            qrResult.setAdapter(tegAdapter);

        } else {
            view = inflater.inflate(R.layout.fragment_page_standart, container, false);

            TextView vie = view.findViewById(R.id.EditfullCodeText);
            vie.setText(mQrText);

        }
        //TextView textView = (TextView) view;
        //textView.setText("Fragment #" + mPage);
        return view;
        //if(contains(C_ORIG_DOC_UNICUM_CODE,'ID')) then substring-after(C_ORIG_DOC_UNICUM_CODE, 'ID') else substring-before(substring-after(C_ORIG_DOC_UNICUM_CODE, 'SUIP'), '#')
    }
}
