package student.adventure;

import java.util.List;

public class GameMap {
    private List<PalletTown> palletTown;

    public PalletTown getPalletTown(int index) {
        return palletTown.get(index);
    }

    public List<PalletTown> getPalletTown() {
        return palletTown;
    }

}
