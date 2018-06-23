package com.pg85.otg.customobjects.bo3;

import com.pg85.otg.LocalWorld;
import com.pg85.otg.OTG;
import com.pg85.otg.customobjects.Branch;
import com.pg85.otg.customobjects.CustomObjectCoordinate;
import com.pg85.otg.exception.InvalidConfigException;
import com.pg85.otg.logging.LogMarker;

import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public class WeightedBranchFunction extends BranchFunction implements Branch
{

    /**
     * At the end of the loading process, this value is equal to the sum of
     * the individual branch chances
     */
    public double cumulativeChance = 0;

    public WeightedBranchFunction(BO3Config config, List<String> args) throws InvalidConfigException
    {
        super(config);
        branches = new TreeSet<BranchNode>();
        cumulativeChance = readArgs(args, true);
    }

    @Override
    public CustomObjectCoordinate toCustomObjectCoordinate(LocalWorld world, Random random, int x, int y, int z)
    {
        double randomChance = random.nextDouble() * (totalChance != -1
                                                     ? totalChance
                                                     : (cumulativeChance >= 100
                                                        ? cumulativeChance
                                                        : 100));
        OTG.log(LogMarker.TRACE, "W-Branch: chance_max - {}", randomChance);
        for (BranchNode branch : branches)
        {
            OTG.log(LogMarker.TRACE, "  {} trying to spawn! #{}", branch.getCustomObject().getName(), branch.getChance());
            if (branch.getChance() >= randomChance)
            {
                OTG.log(LogMarker.TRACE, "  Successful Spawn");
                return new CustomObjectCoordinate(branch.getCustomObject(), branch.getRotation(), x + this.x, y + this.y, z + this.z);
            }
        }
        OTG.log(LogMarker.TRACE, "  No Spawn");
        return null;
    }

    @Override
    protected String getConfigName()
    {
        return "WeightedBranch";
    }

}
