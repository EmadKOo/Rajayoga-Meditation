package emad.youtube.Iterfaces;

import emad.youtube.Model.Favourite.Favourite;
import emad.youtube.Model.Latest.Latest;
import emad.youtube.Model.VideoList.ItemsListData;

public interface ChangeVideo {
    void videoChanged(ItemsListData itemsListData);
    void latestVideoChanged(Latest latestVideo);
    void favouriteVideoChanged(Favourite favourite);
}
