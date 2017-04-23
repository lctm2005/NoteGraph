package com.licong.notemap.service;

import com.licong.notemap.domain.Note;

import java.util.List;

/**
 * Created by lctm2005 on 2017/4/18.
 */
public interface NoteService {

    List<Note> findAll();
}
