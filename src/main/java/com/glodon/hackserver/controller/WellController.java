package com.glodon.hackserver.controller;

import com.glodon.hackserver.bean.GeneralResponse;
import com.glodon.hackserver.bean.Position;
import com.glodon.hackserver.bean.Well;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class WellController {

    public final static CopyOnWriteArrayList<Well> wellList = new CopyOnWriteArrayList<>();

    static {
        wellList.add(new Well("adaec78c-049b-4513-ba51-35b97fae61de", 1890420L, 36.1, 21.4, 1, new Position(382429.1971888808, 329876.68329428934, 10.012970538936358)));
        wellList.add(new Well("1952988", 1952988L, 39.2, 24.6, 1, new Position(390530.33041586645, 305679.6515875374, 10.012972869163427)));
        wellList.add(new Well("1952952", 1952952L, 32.6, 41.2, 1, new Position(396587.8637062001, 300487.9098937023, 10.012972171706195)));
        wellList.add(new Well("1890403", 1890403L, 56.3, 19.4, -1, new Position( 404011.9434993941, 298095.92187530536, 10.012972627055532)));
        wellList.add(new Well("1890418", 1890418L, 36.8, 42.0, 1, new Position(419197.0137434397, 262799.11922147986, 10.01297289095584)));
    }

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @GetMapping("well/{wellId}")
    @ResponseBody
    GeneralResponse<Well> get(@PathVariable("wellId") Long wellId) {
        Optional<Well> optional = wellList.stream()
                .filter(p -> p.getId().equals(wellId))
                .findFirst();

        return optional.map(GeneralResponse::new).orElseGet(GeneralResponse::new);
    }

    @GetMapping("wells")
    @ResponseBody
    GeneralResponse<List<Well>> getWells() {
        return new GeneralResponse<>(wellList);
    }

    @PatchMapping("well")
    @ResponseBody
    GeneralResponse<List<Well>> getList() {
        return new GeneralResponse<>(wellList);
    }

    @PutMapping("well")
    @ResponseBody
    GeneralResponse<Well> update(@RequestBody Well well) {
        Assert.notNull(well, "well must not null");
        Assert.hasText(well.getId(), "well id must not null");

        Optional<Well> optional = wellList.stream()
                .filter(p -> p.getId().equals(well.getId()))
                .findFirst();

        if (optional.isPresent()){
            Well oldWell = optional.get();
            oldWell.setStatus(well.getStatus());
            messagingTemplate.convertAndSend("/wells", wellList);
            return new GeneralResponse<>(oldWell);
        } else {
            throw new RuntimeException("well not found!");
        }
    }
}
