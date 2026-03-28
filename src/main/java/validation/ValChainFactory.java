package validation;

import models.Mission;

public class ValChainFactory {
    public static Validator<Mission> createDefaultChain() {
        MissionBasicValidator basic = new MissionBasicValidator();
        CurseValidator curse = new CurseValidator();
        SorcerersValidator sorcerers = new SorcerersValidator();
        TechniquesValidator techniques = new TechniquesValidator();
        BusinessRulesValidator business = new BusinessRulesValidator();

        basic.setNext(curse);
        curse.setNext(sorcerers);
        sorcerers.setNext(techniques);
        techniques.setNext(business);
        return basic;
    }
}
