package com.grpprj.submit;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import adapters.ChaptersAdapter;
import models.ChaptersModel;

public class ViewChaptersFragment extends Fragment {
    View view;
    RecyclerView rvChaptersList;
    private ArrayList<ChaptersModel> chaptersModelArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_view_chapters, container, false);

        rvChaptersList = view.findViewById(R.id.rvChaptersList);

        chaptersModelArrayList = new ArrayList<>();
        chaptersModelArrayList.add(new ChaptersModel("Chapter 1", "5", false));
        chaptersModelArrayList.add(new ChaptersModel("Chapter 2", "3", false));
        chaptersModelArrayList.add(new ChaptersModel("Chapter 3", "6", false));
        chaptersModelArrayList.add(new ChaptersModel("Chapter 4", "3", false));
        chaptersModelArrayList.add(new ChaptersModel("Chapter 5", "2", false));

        ChaptersAdapter chaptersAdapter = new ChaptersAdapter(getContext(), chaptersModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvChaptersList.setLayoutManager(linearLayoutManager);
        rvChaptersList.setAdapter(chaptersAdapter);
        return view;
    }
}