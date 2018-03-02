package mree.cloud.music.player.common.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mree.cloud.music.player.common.MarkedInfo;

/**
 * Created by mree on 28.02.2016.
 */
public class AudioList extends MarkedInfo {

    private List<SongInfo> list;
    private List<Integer> playOrder;

    public AudioList() {
    }

    public List<SongInfo> getList() {
        return list;
    }

    public void setList(List<SongInfo> list) {
        this.list = list;
    }

    public List<Integer> getPlayOrder() {
        return playOrder;
    }

    public void setPlayOrder(List<Integer> playOrder) {
        this.playOrder = playOrder;
    }

    public List<Integer> generatePlayOrder(boolean isSuffle, int current) {
        List<Integer> orderList = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                orderList.add(i);
            }

            if (isSuffle) {
                Collections.shuffle(orderList);
            }
        }

/*        if (current >= 0) {
            int currentIndex = orderList.indexOf(current);
            Integer first = orderList.get(0);
            orderList.remove(0);
            orderList.add(0, current);
            orderList.remove(currentIndex);
            orderList.add(currentIndex, first);
        }*/

        /*orderList.add(current);
        Random rand = new Random();
        for (int i = 1; i < list.size(); i++) {
            if (isSuffle) {
                Integer randOrder = current;
                while (orderList.size() < list.size() && orderList.contains(randOrder)) {
                    randOrder = rand.nextInt(list.size());
                }
                orderList.add(i, randOrder);
            } else {
                orderList.add(i, i);
            }
        }*/
        return orderList;
    }

    public int getOrderOfSong(SongInfo currentSong) {
        for (int i = 0; i < getPlayOrder().size(); i++) {
            Integer order = getPlayOrder().get(i);
            SongInfo si = getList().get(order);
            if (si.getId().equals(currentSong.getId())) {
                return i;
            }
        }
        return 0;
    }

    public void removeSong(String id) {
        List<Integer> playOrder = getPlayOrder();
        for (int i = 0; i < playOrder.size(); i++) {
            SongInfo si = getList().get(i);
            if (si.getId().equals(id)) {
                getList().remove(si);
                break;
            }
        }
        int i = playOrder.indexOf(playOrder.size() - 1);
        playOrder.remove(i);
    }
}
