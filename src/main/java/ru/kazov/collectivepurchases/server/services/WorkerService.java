package ru.kazov.collectivepurchases.server.services;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;
import ru.kazov.collectivepurchases.protomodels.*;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserItem;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserJob;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserShop;
import ru.kazov.collectivepurchases.server.services.parser.ParserItemService;
import ru.kazov.collectivepurchases.server.services.parser.ParserJobService;

import java.util.List;

@RequiredArgsConstructor
@GrpcService
public class WorkerService extends CPGrpcServiceGrpc.CPGrpcServiceImplBase {

    private final ParserJobService parserJobService;
    private final ParserItemService parserItemService;
    private final PictureService pictureService;


    @Transactional(readOnly = true)
    @Override
    public void fetchJob(Empty request, StreamObserver<ParserJobResponseProto> responseObserver) {
        ParserJob job = parserJobService.getFirstParserJob();
        ParserJobResponseProto jobProto;
        if (job == null)
            jobProto = ParserJobResponseProto.getDefaultInstance();
        else
            jobProto = jobDAOtoPROTO(job);
        responseObserver.onNext(jobProto);
        responseObserver.onCompleted();
    }

    @Transactional()
    @Override
    public void handleError(ErrorRequestProto request, StreamObserver<Empty> responseObserver) {
        parserJobService.setError(request.getParserJobId(), request.getMessage());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Transactional()
    @Override
    public void requestCode(CodeRequestProto request, StreamObserver<CodeResponseProto> responseObserver) {
        //TODO implement method
        super.requestCode(request, responseObserver);
    }

    @Transactional()
    @Override
    public void addItems(AddItemsRequestProto request, StreamObserver<Empty> responseObserver) {
        List<ParserItem> parserItemList = request.getItemsList().stream()
                .map(this::itemPROTOtoDAO)
                .peek((item) -> item.setPictures(item.getPictures().stream()
                            .map(pictureService::storePicture)
                            .toList()))
                .toList();
        parserItemService.createBatchParserItems(parserItemList, request.getParserJobId());
        parserJobService.setComplete(request.getParserJobId());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }



    private ParserJobResponseProto jobDAOtoPROTO(ParserJob parserJob) {
        return ParserJobResponseProto.newBuilder()
                .setId(parserJob.getId())
                .setUrl(parserJob.getUrl())
                .setShop(shopDAOtoPROTO(parserJob.getShop()))
                .build();
    }

    private ParserShopProto shopDAOtoPROTO(ParserShop parserShop) {
        return ParserShopProto.newBuilder()
                .setNeedLogin(parserShop.isNeedLogin())
                .setLogin(parserShop.getLogin())
                .setPassword(parserShop.getPassword())
                .build();
    }

    private ParserItem itemPROTOtoDAO(ParserItemProto parserItemProto) {
        ParserItem parserItem = new ParserItem();
        parserItem.setName(parserItemProto.getName());
        parserItem.setDescription(parserItemProto.getDescription());
        parserItem.setUrl(parserItemProto.getUrl());
        parserItem.setProperties(parserItemProto.getPropertiesMap());
        parserItem.setPrices(parserItemProto.getPricesMap());
        parserItem.setPictures(parserItemProto.getPicturesList().stream().toList());
        return parserItem;
    }
}
