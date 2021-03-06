package com.threecubed.auber;

import com.badlogic.gdx.Gdx;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class AssetTest {
    @Test
    public void assetTest() {
        Assert.assertTrue(Gdx.files.internal("auber.png").exists());
    }

    @Test
    public void spriteTest(){
        Assert.assertTrue(Gdx.files.internal("individual_sprites/alienA.png").exists() && Gdx.files.internal("individual_sprites/alienB.png").exists());
    }
}
