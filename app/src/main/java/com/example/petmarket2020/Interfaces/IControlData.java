package com.example.petmarket2020.Interfaces;

import com.example.petmarket2020.Adapters.items.PosterItem;

import java.util.List;

public interface IControlData {
    default void isSuccessful(boolean isSu) {
    }

    default void responseData(Object data) {
    }
    default void responseData(List<PosterItem> listNormal, List<PosterItem> listHot) {
    }
}
