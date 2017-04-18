package com.licong.wiki.web.controller;

import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.NoteStore;
import com.evernote.edam.type.Note;
import com.evernote.thrift.protocol.TBinaryProtocol;
import com.evernote.thrift.transport.THttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.licong.wiki.util.Href;
import com.licong.wiki.util.XmlUtils;
import com.licong.wiki.web.vo.LinkVo;
import com.licong.wiki.web.vo.NodeVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class NodeController {

    @RequestMapping("/")
    public ModelAndView welcome() throws Exception {

        // Retrieved during authentication:
        String authToken = "S=s31:U=5a2e79:E=162cca47472:C=15b74f34640:P=1cd:A=en-devtoken:V=2:H=a6618a6fdb6611800841e3e17179c18d";
        String noteStoreUrl = "https://app.yinxiang.com/shard/s31/notestore";
        String userAgent = "JamesLee/EverNoteMap/1.0.0";

        THttpClient noteStoreTrans = new THttpClient(noteStoreUrl);
        noteStoreTrans.setCustomHeader("User-Agent", userAgent);
        TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
        NoteStore.Client noteStore = new NoteStore.Client(noteStoreProt, noteStoreProt);
        NoteFilter noteFilter = new NoteFilter();
        noteFilter.setNotebookGuid("dea6691f-cd37-40f9-abbd-b3785da21636");
        NoteList noteList = noteStore.findNotes(authToken, noteFilter, 0, 20);
//        UserStore.Client userStore  = new UserStore.Client(noteStoreProt, noteStoreProt);
//        User user = userStore.getUser(authToken);

        List<NodeVo> nodeVos = new ArrayList<>();
        List<LinkVo> linkVos = new ArrayList<>();

        for (Note note : noteList.getNotes()) {
            NodeVo nodeVo = NodeVo.convert(note);
            nodeVos.add(nodeVo);

            String content = noteStore.getNoteContent(authToken, note.getGuid());

            List<Href> hrefs = XmlUtils.extractHrefs(content);
            for (Href href : hrefs) {
                String url = href.getUrl();
                String value = href.getName();
                if (url.contains("https://app.yinxiang.com")) {
                    // 有效连接
                    LinkVo linkVo = new LinkVo();
                    linkVo.setName(value);
                    linkVo.setSource(nodeVo.getName());
                    linkVo.setTarget(value);
                    linkVo.setWeight(1);
                    linkVos.add(linkVo);
                }
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("welcome");
        ObjectMapper objectMapper = new ObjectMapper();
        modelAndView.addObject("nodes", objectMapper.writer().writeValueAsString(nodeVos));
        modelAndView.addObject("links", objectMapper.writer().writeValueAsString(linkVos));
            return modelAndView;
    }





}
