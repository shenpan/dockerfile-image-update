/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root or
 * https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.dockerfileimageupdate.subcommands.impl;

import com.google.common.collect.ImmutableMap;
import com.salesforce.dockerfileimageupdate.utils.DockerfileGithubUtil;
import net.sourceforge.argparse4j.inf.Namespace;
import org.kohsuke.github.GHRepository;
import org.mockito.Mockito;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;

import static com.salesforce.dockerfileimageupdate.utils.Constants.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;

/**
 * Created by minho.park on 7/19/16.
 */
public class ChildTest {

    @DataProvider
    public Object[][] inputMap() {
        return new Object[][] {
                {Collections.emptyMap()},
                {ImmutableMap.of(
                        GIT_REPO, "test",
                        IMG, "test",
                        FORCE_TAG, "test")},
                {ImmutableMap.of(
                        GIT_REPO, "test",
                        IMG, "test",
                        FORCE_TAG, "test",
                        STORE, "test")},
        };
    }

    @Test(dataProvider = "inputMap")
    public void checkPullRequestMade(Map<String, Object> inputMap) throws Exception {
        Child child = new Child();
        Namespace ns = new Namespace(inputMap);
        DockerfileGithubUtil dockerfileGithubUtil = Mockito.mock(DockerfileGithubUtil.class);
        Mockito.when(dockerfileGithubUtil.getRepo(Mockito.any())).thenReturn(new GHRepository());
        doNothing().when(dockerfileGithubUtil).modifyAllOnGithub(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        doNothing().when(dockerfileGithubUtil).updateStore(anyString(), anyString(), anyString());
        doNothing().when(dockerfileGithubUtil).createPullReq(Mockito.any(), anyString(), Mockito.any(), anyString());

        child.execute(ns, dockerfileGithubUtil);

        Mockito.verify(dockerfileGithubUtil, atLeastOnce())
                .createPullReq(Mockito.any(), anyString(), Mockito.any(), anyString());
    }
}