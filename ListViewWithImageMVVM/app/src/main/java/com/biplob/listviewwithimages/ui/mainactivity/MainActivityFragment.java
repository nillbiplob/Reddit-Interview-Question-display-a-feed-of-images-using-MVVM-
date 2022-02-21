package com.biplob.listviewwithimages.ui.mainactivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aapbd.appbajarlib.network.NetInfo;
import com.aapbd.appbajarlib.storage.PersistData;
import com.aapbd.appbajarlib.storage.PersistObject;
import com.biplob.listviewwithimages.R;
import com.biplob.listviewwithimages.adapters.CharacterListAdapter;
import com.biplob.listviewwithimages.api.ApiResponse;
import com.biplob.listviewwithimages.models.Character;
import com.biplob.listviewwithimages.models.CharacterList;
import com.biplob.listviewwithimages.models.Info;
import com.biplob.listviewwithimages.repository.ViewModelFactory;
import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityFragment extends Fragment {

    @BindView(R.id.btnRefresh)
    ImageButton btnRefresh;

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    @BindView(R.id.resultInfo)
    TextView resultInfo;



    @Inject
    ViewModelFactory viewModelFactory;

    private MainActivityViewModel mViewModel;

    private ProgressDialog progressDialog;

    private ArrayList<Character> characters;


    private CharacterListAdapter characterListAdapter;

    public static String pageCounter="1";
    public static String totalEntry="0";
    public static String totalLoaded="0";
    public static int totalPage=0;


    private Info characterListInfo;



    public static MainActivityFragment newInstance() {
        return new MainActivityFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.main_activity_fragment, container, false);
        ButterKnife.bind(this, fragmentView);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.processing));
        progressDialog.setCancelable(true);

        characters = new ArrayList<>();
        characterListAdapter = new CharacterListAdapter(getActivity(), characters);

        //setting an setOnRefreshListener on the SwipeDownLayout
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {


                if(Integer.parseInt(pageCounter)==totalPage)
                {
                    Toast.makeText(getActivity(), "No more characters.", Toast.LENGTH_SHORT).show();


                }else {
                    Log.e("page counter is ", pageCounter + "");
                    loadMoreData(getActivity(), pageCounter);
                }

            }
        });

        setRetainInstance(true);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel.class);

        mViewModel.response().observe(getViewLifecycleOwner(), new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {
                if (apiResponse != null) {
                    consumeResponse(apiResponse);
                }
            }
        });

        listView.setAdapter(characterListAdapter);


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pageCounter="1";
                loadData(getActivity());
            }
        });

        pageCounter="1";
        loadData(getActivity());
    }


    private void loadData(Context context) {
        if (NetInfo.isOnline(context)) {
            mViewModel.getResponse();


        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();



        }
    }

    private void loadMoreData(Context context, String pageNumber) {
        if (NetInfo.isOnline(context)) {
            mViewModel.getMoreResponse(pageNumber);


        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * method to handle response
     * */
    private void consumeResponse(ApiResponse apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                progressDialog.show();
                break;

            case SUCCESS:
                progressDialog.dismiss();
                if(pullToRefresh.isRefreshing())
                {
                    pullToRefresh.setRefreshing(false);
                }

                Log.d("API call","Done");
                if (apiResponse.data != null) {
                    renderSuccessResponse(apiResponse.data);
                }
                break;

            case ERROR:
                progressDialog.dismiss();
                if(pullToRefresh.isRefreshing())
                {
                    pullToRefresh.setRefreshing(false);
                }
                Toast.makeText(getActivity(), getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    /*
     * method to handle success response
     * */
    private void renderSuccessResponse(CharacterList characterList) {

       // Log.e("data", characterList.toString());

        if (characterList != null) {
            if (characterList.getResults() != null) {

                // store data info Offline






                if(characterList.getInfo()!=null)
                {
                    characterListInfo=characterList.getInfo();


                    // if prev = null, then page counter is 1
                    if(characterListInfo.getNext()==null||characterListInfo.getNext().isEmpty())
                    {


                    }else
                    {

                        Log.e("next link",characterList.getInfo().getNext());

                        pageCounter=characterList.getInfo().getNext().replace("https://rickandmortyapi.com/api/character/?page=","").trim();
                        Log.d("Current page counter", pageCounter+" sfs");

                    }

                    if(pageCounter.equals("1"))
                    {
                        characters=(ArrayList)characterList.getResults();

                    }else
                    {
                        characters.addAll((ArrayList)characterList.getResults());


                    }


                    totalEntry=characterListInfo.getCount()+"";
                    totalLoaded=characters.size()+"";
                    totalPage=characterListInfo.getPages();



                }

                updateCounterView();
                characterListAdapter.updateList(characters);
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCounterView() {


        resultInfo.setText("Total Characters: " + totalEntry+", Total Loaded: "+totalLoaded);



    }

}
