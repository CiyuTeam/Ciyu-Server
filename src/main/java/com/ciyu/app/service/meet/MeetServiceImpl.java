package com.ciyu.app.service.meet;

import com.ciyu.app.pojo.Meet;
import com.ciyu.app.repository.MeetRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class MeetServiceImpl implements MeetService{
    private final MeetRepo meetRepo;

    @Override
    public void saveMeet(Meet meet) {
        meetRepo.save(meet);
    }
}
