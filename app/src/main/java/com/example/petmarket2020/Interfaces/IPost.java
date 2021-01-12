package com.example.petmarket2020.Interfaces;

import com.example.petmarket2020.Adapters.items.PosterItem;

import java.util.List;

public interface IPost {
    default void sendData(Object objData) {
    }

    default void sendData(List<PosterItem> listNormal, List<PosterItem> listHot) {
    }

    default void isSuccessful(boolean isSu) {
    }
}
