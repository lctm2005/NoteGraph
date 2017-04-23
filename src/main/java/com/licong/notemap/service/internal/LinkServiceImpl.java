package com.licong.notemap.service.internal;

import com.licong.notemap.domain.Link;
import com.licong.notemap.repository.LinkRepository;
import com.licong.notemap.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lctm2005 on 2017/4/22.
 */
@Service
public class LinkServiceImpl implements LinkService {
    @Autowired
    private LinkRepository linkRepository;

    @Override
    public List<Link> findAll() {
        Iterable<Link> notes =  linkRepository.findAll();
        Iterator<Link> iterator = notes.iterator();
        List<Link> results = new ArrayList<>();
        while(iterator.hasNext()) {
            Link note = iterator.next();
            results.add(note);
        }
        return results;
    }
}
