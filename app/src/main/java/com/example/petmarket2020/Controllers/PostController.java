package com.example.petmarket2020.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Adapters.LoadMoreHorizontalAdapter;
import com.example.petmarket2020.Adapters.RV_PetCategoryAdapter;
import com.example.petmarket2020.Adapters.RV_PosterAdapter;
import com.example.petmarket2020.Adapters.SliderAdapter;
import com.example.petmarket2020.Adapters.items.PosterItem;
import com.example.petmarket2020.DAL.PostDAL;
import com.example.petmarket2020.DAL.UsersDAL;
import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.HelperClass.PaginationScrollListener;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Interfaces.IPost;
import com.example.petmarket2020.Interfaces.IUsers;
import com.example.petmarket2020.Models.PetTypeModel;
import com.example.petmarket2020.Models.PostModel;
import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.LoginActivity;
import com.example.petmarket2020.Views.PostActivity;
import com.example.petmarket2020.Views.PostDetailActivity;
import com.google.android.material.snackbar.Snackbar;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PostController {
    private static final String KEY_DOG = "dog", KEY_CAT = "cat";
    private final Activity activity;
    private PostDAL postDAL;
    private UsersDAL usersDAL;
    private final PostController postController;

    public PostController(Activity activity) {
        this.activity = activity;
        this.postDAL = new PostDAL();
        this.usersDAL = new UsersDAL(activity);
        this.postController = this;
    }

    private RV_PosterAdapter homeRVPosterAdapter;
    private boolean isLoadingVER;
    private boolean isLastPageVer;

    private LoadMoreHorizontalAdapter loadMoreHorizontalAdapter;
    private boolean isLoadingHor;
    private boolean isLastPageHor;

    public void refreshData() {
        this.postDAL = new PostDAL();
        this.usersDAL = new UsersDAL(activity);
        // refresh data
        // VERTICAL
        homeRVPosterAdapter = null;
        // HORIZONTAL
        loadMoreHorizontalAdapter = null;
        isLoadingVER = isLoadingHor = isLastPageHor = isLastPageVer = false;
    }

    public void getPetBreeds(String type, RadioGroup radioGroup, MyViewPager vpg, TextView tvTitle) {
        if (!PostActivity.isLoaded()) {
            radioGroup.removeAllViews();
            if (PostActivity.getData(type) != null) {
                initView((Map<Integer, String>) Objects.requireNonNull(PostActivity.getData(type)), radioGroup, tvTitle, vpg);
                return;
            }
            postDAL.getPetBreeds(type, new IPost() {
                @Override
                public void sendData(Object objData) {
                    if (objData != null) {
                        Map<Integer, String> dataResp = (Map<Integer, String>) objData;
                        if (type.equals(KEY_DOG) && PostActivity.getData(KEY_DOG) == null) {
                            PostActivity.addData(KEY_DOG, dataResp);
                        } else if (type.equals(KEY_CAT) && PostActivity.getData(KEY_CAT) == null) {
                            PostActivity.addData(KEY_CAT, dataResp);
                        }
                        initView(dataResp, radioGroup, tvTitle, vpg);
                    }
                }
            });
        } else {
            if (radioGroup.getChildCount() == 0) {
                radioGroup.removeAllViews();
                initView((Map<Integer, String>) Objects.requireNonNull(PostActivity.getData((String) PostActivity.getData(PostActivity.KEY_PET_TYPE))), radioGroup, tvTitle, vpg);
            }
        }

    }

    private void initView(Map<Integer, String> dataResp, RadioGroup radioGroup, TextView tvTitle, MyViewPager vpg) {
        RadioButton radioButton;
        ViewGroup.LayoutParams layoutParams = new ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}},
                new int[]{Color.WHITE, Color.BLACK}
        );
        for (Map.Entry<Integer, String> data : dataResp.entrySet()) {
            radioButton = new RadioButton(activity);
            setListener(data.getValue(), radioButton, tvTitle, vpg);
            radioButton.setId(data.getKey());
            radioButton.setText(data.getValue());
            radioButton.setTextColor(colorStateList);
            radioButton.setBackgroundResource(R.drawable.bg_rd_selector);
            radioButton.setTextSize(15);
            radioButton.setLayoutParams(layoutParams);
            radioButton.setButtonDrawable(R.drawable.custom_radio_button);
            radioButton.setPadding(45, 25, 45, 25);
            radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.bg_rd_ic_end_selector, 0);
            radioGroup.addView(radioButton);
        }
        PostActivity.setLoadingStatus(false);
    }

    private void setListener(final String value, RadioButton radioButton, TextView tvTitle, MyViewPager vpg) {
        radioButton.setOnClickListener(v -> {
            PostActivity.addData(PostActivity.KEY_BREEDS, value);
            int currentIndex = vpg.getCurrentItem();
            vpg.setCurrentItem(currentIndex + 1);
            tvTitle.setText(PostActivity.getTitle(currentIndex + 1));
        });
    }

    public void postUpload(PostModel postModel, HashMap<String, byte[]> mapImage, RelativeLayout rlBar, MyViewPager vpg, TextView tvTitles) {
        rlBar.setVisibility(View.VISIBLE);
        postDAL.postUpload(postModel, mapImage, new IPost() {
            @Override
            public void isSuccessful(boolean isSu) {
                if (isSu) {
                    int nextIndex = vpg.getCurrentItem() + 1;
                    vpg.setCurrentItem(nextIndex);
                    tvTitles.setText(PostActivity.getTitle(nextIndex));
                } else {
                    Toast.makeText(activity, "Đã xảy ra lỗi, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
                rlBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void getPetType(RecyclerView rvCategoryDog, RecyclerView rvCategoryCat) {
        rvCategoryCat.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        rvCategoryDog.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        postDAL.getPetType(new IPost() {
            @Override
            public void sendData(Object objData) {
                Object[] objRs = (Object[]) objData;
                List<PetTypeModel> catList = (List<PetTypeModel>) objRs[0];
                List<PetTypeModel> dogList = (List<PetTypeModel>) objRs[1];
                rvCategoryCat.setAdapter(new RV_PetCategoryAdapter(catList));
                rvCategoryDog.setAdapter(new RV_PetCategoryAdapter(dogList));
                rvCategoryDog.setHasFixedSize(true);
                rvCategoryCat.setHasFixedSize(true);
            }
        });

    }

    public void getPostHome(RecyclerView rvPoster, RelativeLayout rlBar, NestedScrollView nestedScrollView, RecyclerView rvHot) {
        rlBar.setVisibility(View.VISIBLE);
        rvPoster.setLayoutManager(new GridLayoutManager(activity, 2));
        List<PosterItem> oldListNormal = new ArrayList<>();
        List<PosterItem> oldListHot = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
        rvHot.setLayoutManager(linearLayoutManager);
        LoadMoreHorizontalAdapter.IOnItemClick iOnItemClickHorizontal = (postId, peType1, price1) -> {
            Intent intent = new Intent(activity, PostDetailActivity.class);
            intent.putExtra("postId", postId);
            intent.putExtra("peType", peType1);
            intent.putExtra("price", price1);
            activity.startActivity(intent);
        };
        RV_PosterAdapter.IOnItemClick iOnItemClick = (postId, peType, price) -> {
            Intent intent = new Intent(activity, PostDetailActivity.class);
            intent.putExtra("postId", postId);
            intent.putExtra("peType", peType);
            intent.putExtra("price", price);
            activity.startActivity(intent);
        };
        /*
        postDAL.getPostHome(int typeRequest,IPost)
        typeRequest = 1 normalList, = 2 hotList, = 0 cả 2 :_)
         */
        isLoadingVER = true;
        postDAL.getPostHome(0, new IPost() {
            @Override
            public void sendData(List<PosterItem> listNormal, List<PosterItem> listHot) {
                // Normal
                oldListNormal.addAll(listNormal);
                homeRVPosterAdapter = new RV_PosterAdapter(listNormal, iOnItemClick);
                homeRVPosterAdapter.setPostController(postController);
                rvPoster.setAdapter(homeRVPosterAdapter);
                rlBar.setVisibility(View.GONE);
                isLoadingVER = false;
                // Hot
                if (!listHot.isEmpty()) {
                    oldListHot.addAll(listHot);
                    loadMoreHorizontalAdapter = new LoadMoreHorizontalAdapter(listHot, iOnItemClickHorizontal);
                    loadMoreHorizontalAdapter.setPostController(postController);
                    loadMoreHorizontalAdapter.addItemLoading();
                    rvHot.setAdapter(loadMoreHorizontalAdapter);
                } else {
                    isLastPageHor = true;
                    loadMoreHorizontalAdapter.removeItemLoading();
                    Toast.makeText(activity, "Hết dữ liệu rồi!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // rvPoster
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {
                if (!isLastPageVer) {
                    if (!isLoadingVER) {
                        rlBar.setVisibility(View.VISIBLE);
                        isLoadingVER = true;
                        postDAL.getPostHome(1, new IPost() {
                            @Override
                            public void sendData(Object objData) {
                                List<PosterItem> posterItems = (List<PosterItem>) objData;
                                if (!posterItems.isEmpty()) {
                                    oldListNormal.addAll(posterItems);
                                    new Handler().postDelayed(() -> {
                                                homeRVPosterAdapter.updateData(oldListNormal);
                                                isLoadingVER = false;
                                                rlBar.setVisibility(View.GONE);
                                            }, 500
                                    );
                                } else {
                                    Toast.makeText(activity, "Hết dữ liệu rồi!!", Toast.LENGTH_SHORT).show();
                                    rlBar.setVisibility(View.GONE);
                                    isLastPageVer = true;
                                }

                            }
                        });
                    }
                }

            }
        });
        // rvHot
        rvHot.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoadingHor = true;
                postDAL.getPostHome(2, new IPost() {
                    @Override
                    public void sendData(Object objData) {
                        List<PosterItem> posterItems = (List<PosterItem>) objData;
                        if (!posterItems.isEmpty()) {
                            new Handler().postDelayed(() -> {
                                oldListHot.addAll(posterItems);
                                loadMoreHorizontalAdapter.removeItemLoading();
                                loadMoreHorizontalAdapter.updateData(oldListHot);
                                isLoadingHor = false;
                                if (posterItems.size() == 4) {
                                    loadMoreHorizontalAdapter.addItemLoading();
                                } else {
                                    isLastPageHor = true;
                                }
                            }, 1000);
                        } else {
                            Toast.makeText(activity, "Hết dữ liệu rồi!!", Toast.LENGTH_SHORT).show();
                            loadMoreHorizontalAdapter.removeItemLoading();
                            isLastPageHor = true;
                        }
                    }
                });
            }

            @Override
            public boolean isLoading() {
                return isLoadingHor;
            }

            @Override
            public boolean isLastPage() {
                return isLastPageHor;
            }
        });
    }

    public void postDetail(String postId, SliderView imageSlider, TextView[] textViews) {
        /*
        textViews:
        0: title
        1: price
        2: breed
        3: gender
        4: age
        5: inject
        6: healthy
        7: phone
        8: are
         */
        postDAL.postDetail(postId, new IPost() {
            @Override
            public void sendData(Object objData) {
                List<Object> list = (List<Object>) objData;
                // 0: PostModel 1: phoneNumber
                PostModel postModel = (PostModel) list.get(0);
                String phoneNumber = list.get(1).toString();
                String poType = postModel.getPoType().contains("bán") ? "[BÁN] " : "[MUA] ";
                String title = poType + postModel.getTitle();
                textViews[0].setText(title);
                textViews[1].setText(Utils.formatCurrencyVN(postModel.getPrice()));
                textViews[2].setText(postModel.getBreed());
                textViews[3].setText(postModel.getGender());
                textViews[4].setText(postModel.getPeAge());
                textViews[5].setText(postModel.getInjectStatus());
                textViews[6].setText(postModel.getHealthGuarantee());
                textViews[7].setText(phoneNumber);
                textViews[8].setText(postModel.getArea());
                List<String> images = postModel.getImages();
                SliderAdapter sliderAdapter = new SliderAdapter();
                sliderAdapter.setStringList(images);
                imageSlider.setSliderAdapter(sliderAdapter);
                imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
                imageSlider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                imageSlider.setScrollTimeInSec(3);
                imageSlider.startAutoCycle();
            }
        });

    }

    public void isFavorite(String postId, ImageView ivFav) {
        if (usersDAL.checkLogin()) {
            HashSet<String> hashSet = (HashSet<String>) usersDAL.getInfo(SessionManager.KEY_FAVORITES, true);
            if (hashSet.contains(postId)) {
                ivFav.setImageResource(R.drawable.ic_item_tym_checked);
                ivFav.setTag(R.id.isChecked, "1"); // checked
            } else {
                ivFav.setTag(R.id.isChecked, "0"); //uncheck
            }
        }
    }

    public void setFavorite(ImageView ivFav, ProgressBar pgBar) {
        if (usersDAL.checkLogin()) {
            ivFav.setVisibility(View.INVISIBLE);
            pgBar.setVisibility(View.VISIBLE);
            String uid = (String) usersDAL.getInfo(SessionManager.KEY_UID, false);
            String postId = (String) ivFav.getTag(R.id.postId);
            boolean isChecked = !ivFav.getTag(R.id.isChecked).toString().equals("0");
            List<String> favorites = new ArrayList<>((HashSet<String>) usersDAL.getInfo(SessionManager.KEY_FAVORITES, true));
            if (isChecked)  // da~ luu
                favorites.remove(postId);
            else  // chua luu
                favorites.add(postId);

            usersDAL.setFavorite(uid, favorites, new IUsers() {
                @Override
                public void isSuccessful(boolean isSu) {
                    if (isSu) {
                        pgBar.setVisibility(View.INVISIBLE);
                        ivFav.setVisibility(View.VISIBLE);
                        if (isChecked) {
                            ivFav.setImageResource(R.drawable.ic_item_tym);
                            ivFav.setTag(R.id.isChecked, "0"); //uncheck
                        } else {
                            ivFav.setImageResource(R.drawable.ic_item_tym_checked);
                            ivFav.setTag(R.id.isChecked, "1"); //check
                            Toast.makeText(activity, "Đã thêm vào mục yêu thích", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            showSBMargin(ivFav);
        }

    }

    private void showSBMargin(View v) {
        Snackbar sb = Snackbar.make(v, "Đăng nhập để tiếp tục lưu tin", Snackbar.LENGTH_LONG)
                .setAction("ĐĂNG NHẬP", v1 -> activity.startActivity(new Intent(activity, LoginActivity.class)));
        sb.setActionTextColor(Color.CYAN);
        sb.setAnchorView(activity.findViewById(R.id.vTmp));
        sb.show();
    }

    public void getSamePosts(String peType, long price, RecyclerView rvSamePost) {
        List<PosterItem> oldSamePostItemList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
        rvSamePost.setLayoutManager(linearLayoutManager);
        LoadMoreHorizontalAdapter.IOnItemClick iOnItemClick = (postId, peType1, price1) -> {
            Intent intent = new Intent(activity, PostDetailActivity.class);
            intent.putExtra("postId", postId);
            intent.putExtra("peType", peType1);
            intent.putExtra("price", price1);
            activity.startActivity(intent);
            activity.finish();
        };
        postDAL.getSamePosts(peType, price, new IPost() {
            @Override
            public void sendData(Object objData) {
                List<PosterItem> posterItems = (List<PosterItem>) objData;
                if (!posterItems.isEmpty()) {
                    oldSamePostItemList.addAll(posterItems);
                    loadMoreHorizontalAdapter = new LoadMoreHorizontalAdapter(posterItems, iOnItemClick);
                    loadMoreHorizontalAdapter.setPostController(postController);
                    if (posterItems.size() == 4) {
                        loadMoreHorizontalAdapter.addItemLoading();
                    } else {
                        Toast.makeText(activity, "Hết dữ liệu rồi!!", Toast.LENGTH_SHORT).show();
                        loadMoreHorizontalAdapter.removeItemLoading();
                        isLastPageHor = true;
                    }
                    rvSamePost.setAdapter(loadMoreHorizontalAdapter);
                } else {
                    Toast.makeText(activity, "Hết dữ liệu rồi!!", Toast.LENGTH_SHORT).show();
                    loadMoreHorizontalAdapter.removeItemLoading();
                    isLastPageHor = true;
                }
            }
        });
        rvSamePost.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoadingHor = true;
                postDAL.getSamePosts(peType, price, new IPost() {
                    @Override
                    public void sendData(Object objData) {
                        List<PosterItem> itemsLoading = (List<PosterItem>) objData;
                        if (!itemsLoading.isEmpty()) {
                            new Handler().postDelayed(() -> {
                                oldSamePostItemList.addAll(itemsLoading);
                                loadMoreHorizontalAdapter.removeItemLoading();
                                loadMoreHorizontalAdapter.updateData(oldSamePostItemList);
                                isLoadingHor = false;
                                if (itemsLoading.size() == 4) {
                                    loadMoreHorizontalAdapter.addItemLoading();
                                } else {
                                    Toast.makeText(activity, "Hết dữ liệu rồi!!", Toast.LENGTH_SHORT).show();
                                    isLastPageHor = true;
                                }
                            }, 1000);
                        } else {
                            Toast.makeText(activity, "Hết dữ liệu rồi!!", Toast.LENGTH_SHORT).show();
                            loadMoreHorizontalAdapter.removeItemLoading();
                            isLastPageHor = true;
                        }
                    }
                });
            }

            @Override
            public boolean isLoading() {
                return isLoadingHor;
            }

            @Override
            public boolean isLastPage() {
                return isLastPageHor;
            }
        });

    }
}