package edu.utfpr.importer.service;

import java.util.List;

import javax.persistence.EntityManager;

import edu.utfpr.importer.model.BiddingParticipant;
import edu.utfpr.importer.parser.SAXParser;
import edu.utfpr.importer.persistence.provider.HibernateProvider;
import edu.utfpr.importer.xml.handler.BiddingParticipantSAXHandler;

public class BiddingParticipantServiceImpl implements BiddingService<BiddingParticipant> {

    private final SAXParser<BiddingParticipant> xmlParser = new SAXParser<BiddingParticipant>();
    private final EntityManager entityManager = HibernateProvider.getInstance().getEntityManager();

    private final EntityService entityService = new EntityService();

    public List<BiddingParticipant> parseXML(final String path, final String fileName) throws Exception {
        return xmlParser.parseFile(path, fileName, BiddingParticipantSAXHandler.class);
    }

    public void save(final List<BiddingParticipant> participants) {
        for (BiddingParticipant participant : participants) {
            save(participant);
        }
    }

    public void save(final BiddingParticipant participant) {
        try {
            entityService.save(participant.getPk().getEntity());

            entityManager.getTransaction().begin();
            entityManager.merge(participant);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Bidding: " + participant.getPk().getBidding().getBidId() + " docNumber: "
                    + participant.getPk().getEntity().getDocumentNumber());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        entityManager.close();
    }

    @Override
    public String toString() {
        return "Participants";
    }
}
