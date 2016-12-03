package hu.ait.android.mobilefinalproject.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.adapter.ClumpRecyclerAdapter;
import hu.ait.android.mobilefinalproject.adapter.MainPagerAdapter;
import hu.ait.android.mobilefinalproject.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClumpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClumpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClumpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = "ClumpFragment";
    private RecyclerView recyclerView;
    private ClumpRecyclerAdapter clumpRecyclerAdapter;
    private View root;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClumpFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ClumpFragment.
//     */
//    // TODO: Rename and change types and number of parameters
////    public static ClumpFragment newInstance(String param1, String param2) {
////        ClumpFragment fragment = new ClumpFragment();
////        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1, param1);
////        args.putString(ARG_PARAM2, param2);
////        fragment.setArguments(args);
////        return fragment;
////    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_clump, container, false);

        setupRecyclerView();

        return root;
    }

    private void setupRecyclerView() {
//        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerClump);
//        recyclerView.setHasFixedSize(true);
//        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//
//        clumpRecyclerAdapter = new ClumpRecyclerAdapter(getContext(), new User("me", "also me"));
//
//        recyclerView.setAdapter(clumpRecyclerAdapter);

        clumpRecyclerAdapter = new ClumpRecyclerAdapter(getContext(), new User("User", "user"));
        RecyclerView recyclerViewPlaces = (RecyclerView) root.findViewById(
                R.id.recyclerClump);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewPlaces.setLayoutManager(layoutManager);
        recyclerViewPlaces.setAdapter(clumpRecyclerAdapter);

    }

}
