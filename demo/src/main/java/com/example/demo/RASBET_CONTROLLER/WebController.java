package com.example.demo.RASBET_CONTROLLER;

import com.example.demo.RASBET_LN.IRASBET_Facade;
import com.example.demo.RASBET_LN.RASBET_Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class WebController {
    private IRASBET_Facade rasbet_facade;

    @Autowired
    public WebController(RASBET_Facade rb) {
        this.rasbet_facade = rb;
    }

    @GetMapping("/")
    public ModelAndView index()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Login");
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Register1");
        return modelAndView;
    }

    @GetMapping("/error1")
    public ModelAndView error()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Error");
        return modelAndView;
    }

    /**
     * Function that allows to get all the open games.
     * It returns a list of games
     * @return List<GameDTO>
     */
    public List<GameDTO> get_All_Games(){
        List<GameDTO> games = new ArrayList<>();
        List<List<Map<String,String>>> l = rasbet_facade.get_All_Games();
        for (List<Map<String,String>> l1 : l){ //
            games.add(new GameDTO(l1));
        }
        return games;
    }

    /**
     * Function that allows to get the games of a sport
     * It returns a list of games, or a empty list
     * @param sport String
     * @return List<GameDTO>
     */
    public List<GameDTO> get_Games_of_Sport(String sport){
        List<GameDTO> games = new ArrayList<>();
        List<List<Map<String,String>>> l = rasbet_facade.get_Games_of_Sport(sport);
        for (List<Map<String,String>> l1 : l){
            games.add(new GameDTO(l1));
        }
        return games;
    }

    public void update_following_gamedto(List<GameDTO> games, List<String> idGame){

        for (GameDTO g: games){
            if (idGame.contains(g.getIdGame())){
                g.setFollowed(true);
            }
        }
    }


    @PostMapping("/home")
    public ModelAndView home(@RequestBody Map<String,String> payload)
    {
        ModelAndView model = new ModelAndView();
        if (rasbet_facade.login(payload.get("email"), payload.get("password"))) {
            try {
                if (rasbet_facade.getRole(payload.get("email")).equals("user")) {
                    model.setViewName("Sports");
                    List<String> not = this.rasbet_facade.generateNotification(payload.get("email"));
                    if(!not.isEmpty()){
                        model.addObject("notification", not);
                    }
                    try {
                        BetDTO betUpdated = new BetDTO(this.rasbet_facade.showBet(payload.get("email")));
                        model.addObject("current_bet",betUpdated);
                    } catch (Exception e) {}
                }
                else if (rasbet_facade.getRole(payload.get("email")).equals("admin")){
                    model.setViewName("Admin_Sports");
                }
                else if (rasbet_facade.getRole(payload.get("email")).equals("specialist")){
                    model.setViewName("Specialist");
                }

                List<GameDTO> games = get_All_Games();
                List<String> followed = this.rasbet_facade.get_followed_games(payload.get("email"));
                update_following_gamedto(games, followed);
                model.addObject("games", games);
                model.addObject("name", rasbet_facade.getName(payload.get("email")));
                model.addObject("email", payload.get("email"));
            } catch (Exception e) {}
        }
        else model.setViewName("redirect:/login");
        return model;
    }

    //------------USER------------------

    @PostMapping("/register/home")
    public ModelAndView register(@RequestBody UserDTO user){
        ModelAndView model = new ModelAndView();
        boolean b = false;
        try {
           b = rasbet_facade.registerUser(user.getEmail(), user.getName(), user.getPwd(), user.getAddress(), user.getPhone_number(), user.getNif(), user.getCc(), user.getBirth(), user.getCodeUser());
        } catch (Exception e) {
            model.setViewName("redirect:/register");
            return model;
        }
        if(b){
            model.setViewName("Sports");
            model.addObject("email", user.getEmail());
            model.addObject("name", user.getName());

            List<GameDTO> games = get_All_Games();
            List<String> followed = this.rasbet_facade.get_followed_games(user.getEmail());
            update_following_gamedto(games, followed);
            model.addObject("games", games);

            try {
                BetDTO betUpdated = new BetDTO(this.rasbet_facade.showBet(user.getEmail()));
                model.addObject("current_bet",betUpdated);
            } catch (Exception e) {}
        }
        return model;
    }

    @PostMapping("/home/sports")
    public ModelAndView getFootballGames(@RequestBody Map<String, String> payload){
        ModelAndView model = new ModelAndView();
        String email = payload.get("email"); //Email of the user to whom the bet belongs
        String nameSport = payload.get("SportName");
        model.setViewName("Sports");

        model.addObject("email", email);
        try {
            String name = this.rasbet_facade.getName(email);
            model.addObject("name", name);
        } catch (Exception e) {}

        List<GameDTO> games = new ArrayList<>();
        if(nameSport.equals("AllSports")){
            games = get_All_Games();
        }
        else {
            games = get_Games_of_Sport(nameSport);
        }

        List<String> followed = this.rasbet_facade.get_followed_games(email);
        update_following_gamedto(games, followed);
        model.addObject("games", games);

        try {
            BetDTO betUpdated = new BetDTO(this.rasbet_facade.showBet(email));
            model.addObject("current_bet",betUpdated);
        } catch (Exception e) {}
        return model;
    }

    @PostMapping("/home/betHistory")
    public ModelAndView betsHistory(@RequestBody String email){
        ModelAndView model = new ModelAndView();

        //List with all the bets:
        List<BetDTO> betdto = new LinkedList<>();

        List<List<List<String>>> bets = rasbet_facade.consult_Bets_History(email);
        for(List<List<String>> b: bets){
            BetDTO newStruct = new BetDTO(b);
            betdto.add(newStruct);
        }

        model.setViewName("Bets");
        model.addObject("json", betdto);
        try {
            String name = this.rasbet_facade.getName(email);
            model.addObject("name", name);
        } catch (Exception e) {}
        return model;
    }

    @PostMapping("/home/wallet")
    public ModelAndView showwallet(@RequestBody Map<String,String> payload){
        String email = payload.get("email");
        System.out.println("wallet:"+email);
        ModelAndView model = new ModelAndView();
        Map<String,String> wallet = null;
        WalletDTO w = null;
        try {
            wallet = rasbet_facade.consult_Wallet(email);
            w = new WalletDTO(wallet);
            System.out.println(w.getBalance());
        } catch (Exception e) {
            model.setViewName("redirect:/error");
            return model;
        }
        if(w!=null){
            model.setViewName("Profile.html");
            model.addObject("json", w);
            try {
                String name = this.rasbet_facade.getName(email);
                model.addObject("name", name);
                String codAmigo = this.rasbet_facade.getFriendBonus(email);
                model.addObject("friendcode", codAmigo);
            }catch (Exception e){}
        }
        return model;
    }

    @PostMapping("/home/wallet/deposit")
    public void deposit(@RequestBody Map<String, String> payload){
        try {
            rasbet_facade.deposit_money(payload.get("email"), Float.valueOf(payload.get("value")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/home/wallet/withdraw")
    public void withdraw(@RequestBody Map<String, String> payload){
        try {
            rasbet_facade.withdraw_money(payload.get("email"), Float.valueOf(payload.get("value")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/home/profile/token")
    public String getTokenProfile(@RequestBody Map<String, String> payload){
        String email = payload.get("email");
        String tkn;
        try{
            tkn = this.rasbet_facade.getTokenIdentification(email);
        }
        catch (Exception e){
            tkn="";
        }

        return tkn;
    }

    @PostMapping("/home/profile/edit")
    public void editProfile(@RequestBody Map<String, String> payload){
        String email = payload.get("email");
        if (!payload.get("new_name").equals("")) {
            try {
                rasbet_facade.changeName(email, payload.get("new_name"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                String msg = "Error: Name not changed.";
            }
        }
        if (!payload.get("new_email").equals("")) {
            boolean b;
            try {
                b = rasbet_facade.changeEmail(email, payload.get("new_email"));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                b = false;
                String msg = "Error: Email not changed.";
            }
            if (b) {
                email = payload.get("new_email");
            }
        }
        if (!payload.get("new_password").equals("")) {
            try {
                rasbet_facade.changePwd(email, payload.get("new_password"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                String msg = "Error: Password not changed.";
            }
        }

        String tkn = "";
        tkn = payload.get("token");

        if (!payload.get("new_address").equals("")) {
            try {
                rasbet_facade.changeAddress(email, payload.get("new_address"), tkn);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                String msg = "Error: Address not changed.";
            }
        }

        if (!payload.get("new_phone").equals("")) {
            try {
                rasbet_facade.changePhoneNumber(email, payload.get("new_phone"), tkn);
            } catch (Exception e){
                System.out.println(e.getMessage());
                String msg = "Error: Phone Number not changed.";
            }
        }
        this.rasbet_facade.removeToken(email);
    }

    @PostMapping("/home/profile/currency")
    public void changeCurrency(@RequestBody Map<String, String> payload) throws Exception {
        String email = payload.get("email");
        if (payload.get("new_coin")!=null) rasbet_facade.changeCoin(email, payload.get("new_coin"));
    }

    @PostMapping("/home/wallet/transactions")
    public ModelAndView transaction(@RequestBody Map<String, String> payload){
        ModelAndView model = new ModelAndView();
        List<TransactionDTO> transactions = new ArrayList<>();
        for (Map<String,String> e : rasbet_facade.getTransactions(payload.get("email"))){
            for (Map.Entry e1 : e.entrySet()) {
                TransactionDTO t = new TransactionDTO(e1);
                transactions.add(t);
            }
        }
        model.setViewName("Transactions");
        model.addObject("transactions", transactions);
        float currentBalance = this.rasbet_facade.getBalance(payload.get("email"));
        model.addObject("currentBalance", currentBalance);
        try {
            String name = this.rasbet_facade.getName(payload.get("email"));
            model.addObject("name", name);
        } catch (Exception e) {}
        return model;
    }

    @PostMapping("/home/bet")
    public int bet(@RequestBody Map<String, String> payload){
        //Only expects to receive a guess, not the whole bet:
        String email = payload.get("email"); //Email of the user to whom the bet belongs
        String idGame = payload.get("idGame"); //Id the game to which the bet belongs to
        String guess = payload.get("guess"); //Guess should be either x or the index of the participant

        try {
            rasbet_facade.add_to_bet(email,idGame,guess);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        //Bet updated (may be used to reload the UI)
        BetDTO betUpdated = new BetDTO(this.rasbet_facade.showBet(email));
        return 0;
    }

    @PostMapping("/home/bet/payWallet")
    public void payBetFromWallet (@RequestBody Map<String, String> payload){
        String email = payload.get("email"); //Email of the user to whom the bet belongs
        float money =  Float.valueOf(payload.get("money"));
        try {
            rasbet_facade.setBetWallet(email,money); //Pay from Wallet
        }catch (Exception e){
            //If it is not possible to pay, because of the state of a few games,
            //The new bet will not have the games that cause problems
            System.out.println(e.getMessage());
        }
        //Bet updated (may be used to reload the UI)
        BetDTO betUpdated = new BetDTO(this.rasbet_facade.showBet(email));
    }

    @PostMapping("/home/bet/payTransference")
    public void payBetFromTransference (@RequestBody Map<String, String> payload){
        String email = payload.get("email"); //Email of the user to whom the bet belongs
        float money =  Float.valueOf(payload.get("money"));
        try {
            rasbet_facade.setBetPayMethod(email,money); //Pay from Other Pay Methods
        }catch (Exception e){
            //If it is not possible to pay, because of the state of a few games,
            //The new bet will not have the games that cause problems
            System.out.println(e.getMessage());
        }
        //Bet updated (may be used to reload the UI)
        BetDTO betUpdated = new BetDTO(this.rasbet_facade.showBet(email));
    }

    @PostMapping("/home/bet/remove")
    public void removeFromBet(@RequestBody Map<String, String> payload){
        String email = payload.get("email"); //Email of the user to whom the bet belongs
        String idGame = payload.get("idGame");
        try {
            rasbet_facade.removeFromBet(email, idGame);
        }catch (Exception e){
            //If it is not possible to pay, because of the state of a few games,
            //The new bet will not have the games that cause problems
            System.out.println(e.getMessage());
        }
        //Bet updated (may be used to reload the UI)
        BetDTO betUpdated = new BetDTO(this.rasbet_facade.showBet(email));
    }

    @PostMapping("/home/sports/follow")
    public void followGame(@RequestBody Map<String, String> payload){
        String email = payload.get("email"); //Email of the user to whom the bet belongs
        String idGame = payload.get("idGame");
        boolean b = rasbet_facade.startFollowingGame(email,idGame);
    }

    @PostMapping("/home/sports/unfollow")
    public void unfollowGame(@RequestBody Map<String, String> payload){
        String email = payload.get("email"); //Email of the user to whom the bet belongs
        String idGame = payload.get("idGame");
        boolean b = rasbet_facade.stopFollowingGame(email,idGame);
    }

    //---------------SPECIALIST------------------

    @PostMapping("/home/specialist/Sports")
    public ModelAndView getFootballGamesSpec(@RequestBody Map<String, String> payload){
        ModelAndView model = new ModelAndView();
        String email = payload.get("email"); //Email of the user to whom the bet belongs
        String nameSport = payload.get("SportName");
        model.setViewName("Specialist");
        model.addObject("email", email);
        try {
            String name = this.rasbet_facade.getName(email);
            model.addObject("name", name);
        } catch (Exception e) {}


        List<GameDTO> games = new ArrayList<>();
        if(nameSport.equals("AllSports")){
            games = get_All_Games();
        }
        else {
            games = get_Games_of_Sport(nameSport);
        }
        model.addObject("games", games);

        return model;
    }

    @PostMapping("/home/specialist/manageOdds/changeOdd")
    public ModelAndView changeOdds(@RequestBody Map<String, String> payload) throws Exception {
        if((payload.get("participant")).equals("Draw")){
            rasbet_facade.changeOddDrawGame(payload.get("idGame"), Float.valueOf(payload.get("newOdd")), true);
        }
        else {
            rasbet_facade.changeOdd(payload.get("idGame"), Integer.parseInt(payload.get("participant")), Float.valueOf(payload.get("newOdd")), true);
        }
        List<GameDTO> games = get_All_Games();
        ModelAndView m = new ModelAndView();
        m.setViewName("Specialist");
        m.addObject("games", games);
        return m;
    }

    //---------------ADMIN------------------

    @PostMapping("/home/admin/Sports")
    public ModelAndView getGamesAdmin(@RequestBody Map<String, String> payload){
        ModelAndView model = new ModelAndView();
        String email = payload.get("email"); //Email of the user to whom the bet belongs
        String nameSport = payload.get("SportName");
        model.setViewName("Admin_Sports");
        model.addObject("email", email);
        try {
            String name = this.rasbet_facade.getName(email);
            model.addObject("name", name);
        } catch (Exception e) {}


        List<GameDTO> games = new ArrayList<>();
        if(nameSport.equals("AllSports")){
            games = get_All_Games();
        }
        else {
            games = get_Games_of_Sport(nameSport);
        }
        model.addObject("games", games);

        return model;
    }

    @PostMapping("/home/admin/manageGames/{state}Game")
    public String changeStateGame(@PathVariable String state, @RequestBody Map<String,String> payload) throws Exception {
        System.out.println(state);
        try {
            if (state.equals("open")) {
                rasbet_facade.openGame(payload.get("idGame"));
            } else if (state.equals("close")) {
                rasbet_facade.closeGame(payload.get("idGame"));
            } else if (state.equals("suspend")) {
                rasbet_facade.suspendGame(payload.get("idGame"));
            }
        }
        catch (Exception e){
            return e.getMessage();
        }
        return "State of game successfully updated.";
    }

    @PostMapping("/home/admin/manageGames/changeResult")
    public void changeResult(@RequestBody Map<String,String> payload) {
        try {
            boolean b = rasbet_facade.addResult(payload.get("idGame"), payload.get("result"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @PostMapping("/home/admin/promos")
    public ModelAndView getPromotions(@RequestBody String email){
        ModelAndView model = new ModelAndView();
        model.setViewName("Admin_Promocoes");
        model.addObject("email", email);
        try {
            String name = this.rasbet_facade.getName(email);
            model.addObject("name", name);
        } catch (Exception e) {}

        List<PromotionDTO> promos = new ArrayList<>();
        List<Map<String,String>> map = this.rasbet_facade.getPromotions();
        for (Map<String,String> map1 : map){
            for (Map.Entry e : map1.entrySet()){
                promos.add(new PromotionDTO(e));
            }
        }
        model.addObject("promotions", promos);
        return model;
    }

    @PostMapping("/home/admin/promos/activatePromo{promo}")
    public ModelAndView activatePromos(@PathVariable String promo) {
        if (promo.equals("bonus"))
            rasbet_facade.activatePromoBonus();
        else if (promo.equals("multi"))
            rasbet_facade.activatePromoMultiplier();
        List<PromotionDTO> promos = new ArrayList<>();
        List<Map<String,String>> map = this.rasbet_facade.getPromotions();
        for (Map<String,String> map1 : map){
            for (Map.Entry e : map1.entrySet()){
                promos.add(new PromotionDTO(e));
            }
        }
        ModelAndView model = new ModelAndView();
        model.setViewName("Admin_Promocoes");
        model.addObject("promotions", promos);
        return model;
    }

    @PostMapping("/home/admin/promos/deactivatePromo{promo}")
    public ModelAndView deactivatePromos(@PathVariable String promo){
        if(promo.equals("bonus"))
            rasbet_facade.deactivatePromoBonus();
        else if(promo.equals("multi"))
            rasbet_facade.deactivatePromoMultiplier();
        List<PromotionDTO> promos = new ArrayList<>();
        List<Map<String,String>> map = this.rasbet_facade.getPromotions();
        for (Map<String,String> map1 : map){
            for (Map.Entry e : map1.entrySet()){
                promos.add(new PromotionDTO(e));
            }
        }
        ModelAndView model = new ModelAndView();
        model.setViewName("Admin_Promocoes");
        model.addObject("promotions", promos);
        return model;
    }

    @PostMapping("/admin_manage")
    public ModelAndView getPageRegisterStaff(@RequestBody String email){
        ModelAndView model = new ModelAndView();
        model.setViewName("Admin_Manage");
        model.addObject("email", email);
        try {
            String name = this.rasbet_facade.getName(email);
            model.addObject("name", name);
        } catch (Exception e) {}

        return model;
    }

    @PostMapping("/register/home/admin")
    public String registerAdmin(@RequestBody UserDTO user){
        String res="";
        boolean b = false;
        try {
            b = rasbet_facade.registerAdmin(user.getEmail(), user.getName(), user.getPwd(), user.getAddress(), user.getPhone_number(), user.getNif(), user.getCc(), user.getBirth());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            res = "Impossible to create user. "+e.getMessage();
        }
        if(b){
            res = "Successful. User "+user.getName()+" was created.";
        }
        else{
            res = "Impossible to create user.";
        }
        return res;
    }

    @PostMapping("/register/home/specialist")
    public ModelAndView registerSpecialist(@RequestBody UserDTO user){
        //System.out.println(user.getRole());
        ModelAndView model = new ModelAndView();
        boolean b = false;
        try {
            b = rasbet_facade.registerSpecialist(user.getEmail(), user.getName(), user.getPwd(), user.getAddress(), user.getPhone_number(), user.getNif(), user.getCc(), user.getBirth());
        } catch (Exception e) {
            model.setViewName("redirect:/error");
            return model;
        }
        if(b){
            model.setViewName("Test"); //mudar dps
            model.addObject("json", user.getEmail());
        }
        return model;
    }
}

