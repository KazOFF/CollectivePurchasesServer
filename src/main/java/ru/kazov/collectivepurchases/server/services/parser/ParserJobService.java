package ru.kazov.collectivepurchases.server.services.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazov.collectivepurchases.server.common.exceptions.CategoryNotFoundException;
import ru.kazov.collectivepurchases.server.common.exceptions.JobNotFoundException;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserJob;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserJobStatus;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserShop;
import ru.kazov.collectivepurchases.server.repositories.ParserCategoryRepository;
import ru.kazov.collectivepurchases.server.repositories.ParserJobRepository;
import ru.kazov.collectivepurchases.server.repositories.ParserShopRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ParserJobService {
    private final ParserJobRepository parserJobRepository;
    private final ParserCategoryRepository parserCategoryRepository;
    private final ParserShopRepository parserShopRepository;

    @Transactional(readOnly = true)
    public List<ParserJob> getAllParserJobs() {
        return parserJobRepository.findAll(Sort.by("updatedAt").descending());
    }

    @Transactional(readOnly = true)
    public ParserJob getFirstParserJob() {
        ParserJob parserJob = parserJobRepository.findFirstPending().orElse(null);

        if (parserJob != null) {
            parserJob.setStatus(ParserJobStatus.WORKING);
            parserJobRepository.save(parserJob);
        }
        return parserJob;
    }

    @Transactional
    public ParserJob getParserJob(Long parserJobId) {
        return parserJobRepository.findById(parserJobId).orElseThrow(JobNotFoundException::new);
    }

    @Transactional
    public Long createParserJob(ParserJob parserJob) {
        ParserShop parserShop = parserShopRepository.findById(parserJob.getShop().getId()).orElseThrow(CategoryNotFoundException::new);
        parserJob.setShop(parserShop);

        parserJob.setStatus(ParserJobStatus.PENDING);
        parserJobRepository.saveAndFlush(parserJob);
        return parserJob.getId();
    }

    @Transactional
    public void setPending(Long parserJobId) {
        ParserJob parserJob = parserJobRepository.findById(parserJobId).orElseThrow(JobNotFoundException::new);
        parserJob.setStatus(ParserJobStatus.PENDING);
        parserJob.setMessage("");
        parserJobRepository.saveAndFlush(parserJob);
    }

    @Transactional
    public void setOnWork(Long parserJobId) {
        ParserJob parserJob = parserJobRepository.findById(parserJobId).orElseThrow(JobNotFoundException::new);
        parserJob.setStatus(ParserJobStatus.WORKING);
        parserJobRepository.save(parserJob);
    }

    @Transactional
    public void setComplete(Long parserJobId) {
        ParserJob parserJob = parserJobRepository.findById(parserJobId).orElseThrow(JobNotFoundException::new);
        parserJob.setStatus(ParserJobStatus.DONE);
        parserJobRepository.save(parserJob);
    }

    @Transactional
    public void setError(Long parserJobId, String message) {
        ParserJob parserJob = parserJobRepository.findById(parserJobId).orElseThrow(JobNotFoundException::new);
        parserJob.setStatus(ParserJobStatus.ERROR);
        parserJob.setMessage(message);
        parserJobRepository.save(parserJob);
    }

    @Transactional
    public void removeParserJob(Long parseJobId) {
        parserJobRepository.deleteById(parseJobId);
    }

}
