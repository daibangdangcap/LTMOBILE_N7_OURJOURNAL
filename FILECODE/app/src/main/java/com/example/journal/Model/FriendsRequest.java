package com.example.journal.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journal.Adapter.FriendsListAdapter;
import com.example.journal.Adapter.FriendsRequestAdapter;
import com.example.journal.R;

import java.util.ArrayList;

public class FriendsRequest {
    public FriendsRequest(String avatar_friendrequest, String nameuser_friendrequest) {
        this.avatar_friendrequest = avatar_friendrequest;
        this.nameuser_friendrequest = nameuser_friendrequest;
    }

    String avatar_friendrequest;

    public String getAvatar_friendrequest() {
        return avatar_friendrequest;
    }

    public void setAvatar_friendrequest(String avatar_friendrequest) {
        this.avatar_friendrequest = avatar_friendrequest;
    }

    public String getNameuser_friendrequest() {
        return nameuser_friendrequest;
    }

    public void setNameuser_friendrequest(String nameuser_friendrequest) {
        this.nameuser_friendrequest = nameuser_friendrequest;
    }

    String nameuser_friendrequest;

    public FriendsRequest() {
    }
}
