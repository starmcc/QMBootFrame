package com.qm.frame.basic.init;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author 浅梦工作室
 * @createDate 2018年11月18日17:56:56
 * @Description 项目启动时，初始化某些设定
*/
public @Component class InitStartApi {
	
	private final static Logger LOG = LoggerFactory.getLogger(InitStartApi.class);
	
	/**
	 * @Title: init
	 * @Description: 项目启动时，只执行一次
	 */
	@PostConstruct
	public void init() {
		LOG.info("                                                       \r\n" + 
				"                                                       \r\n" + 
				"                                 @                     \r\n" + 
				"                                 MG. :                 \r\n" + 
				"                               ,ZvZ:S@                 \r\n" + 
				"                            .r,k1U5:O7E     r          \r\n" + 
				"           Yv  Z8 ,  Z@      5@,.1@@7:@Bi :@           \r\n" + 
				"            7@B:U7@@J1@u     .G@U:@L.Mi;MB@r           \r\n" + 
				"             iiJFiL@i.FqJ7   :7 MvUuFriuu8u;           \r\n" + 
				"            N2:,vkrj::uu @@  FZ:,5qP:5PuMJYF           \r\n" + 
				"             i7vi,7ELriX,.@@@rr@k:LFrLJMk:Oi        v  \r\n" + 
				"             5LiF0LLZ5,ju:rv.7SYPSuquuGk:75M  i0@EB@i  \r\n" + 
				"          .r@qviY5:,iX577:i5r,X1:r2OruP77Eur@@@52vX    \r\n" + 
				"              PBL75v7.r@LiivuJrvqOUPLEvi8uiukv:5Lr0:   \r\n" + 
				"                :rurUqB@@8irU,iv7:rZOr;N7iFkYOM2i2@i   \r\n" + 
				"          @SL:. :ZvE@ME::1qur:17JL70r:N7,XBUMSruM@7    \r\n" + 
				"      .iiO8LFBSuuL::7UkUJiiMJrJirv7Oq7:,Pqu@r:iv5@@i   \r\n" + 
				"  .:ruSSF:v,.:u257uq;i7r7XLYqM.rLJvOi,iNq5M7v1U8M7     \r\n" + 
				"    ,kYvFujXLJ,7S5,:F::NPFq21XE:v7vZirUu08:7UvG5       \r\n" + 
				"      ZS7vYY7uN27EO7:J:L@vL1jLk@::7X,rF@Eiu02@i        \r\n" + 
				"       :17r7vr::i,rNNO87JU7:u57OO:L2:0XLruLJiO         \r\n" + 
				"         2rr;vvvvY7ii.:L2qqqPNviu5ruUr7OGMOu@@@@,      \r\n" + 
				"         8MMNP5v:rrirJv7i,r7:7Yk7u@@M@@@@@OBJ;iXr      \r\n" + 
				"       .r. ...rvvr7Y7.:r:7rvSNXJG@5,                   \r\n" + 
				"               7j,,i2quLXNYjMSrvM@  LJ                 \r\n" + 
				"              .L:M@7i@@ikY7qv,U@@    .M:               \r\n" + 
				"              XvY7qJ7vSF8@@BUk@@       7P              \r\n" + 
				"            :@5LjS5kB@@qk: @u@@          5u            \r\n" + 
				"           :J       G@     .@Y             Nv          \r\n" + 
				"                           2,               :Gr        \r\n" + 
				"                          .                   ;M:      \r\n" + 
				"                                                jB     \r\n" + 
				"                                                  @k   \r\n" + 
				"                                                   @.  \r\n" + 
				"  .g8\"\"8q. `7MMM.     ,MMF'    `7MM\"\"\"YMM `7MM\"\"\"Mq.        db      `7MMM.     ,MMF'`7MM\"\"\"YMM  \r\n" + 
				".dP'    `YM. MMMb    dPMM        MM    `7   MM   `MM.      ;MM:       MMMb    dPMM    MM    `7  \r\n" + 
				"dM'      `MM M YM   ,M MM        MM   d     MM   ,M9      ,V^MM.      M YM   ,M MM    MM   d    \r\n" + 
				"MM        MM M  Mb  M' MM        MM\"\"MM     MMmmdM9      ,M  `MM      M  Mb  M' MM    MMmmMM    \r\n" + 
				"MM.      ,MP M  YM.P'  MM        MM   Y     MM  YM.      AbmmmqMA     M  YM.P'  MM    MM   Y  , \r\n" + 
				"`Mb.    ,dP' M  `YM'   MM        MM         MM   `Mb.   A'     VML    M  `YM'   MM    MM     ,M \r\n" + 
				"  `\"bmmd\"' .JML. `'  .JMML.    .JMML.     .JMML. .JMM..AMA.   .AMMA..JML. `'  .JMML..JMMmmmmMMM \r\n" + 
				"      MMb                                                                                       \r\n" + 
				"       `bood'                                                                                   "
				+ "\r\n浅梦gitHub:https://github.com/starmcc/QMBoootFrame");

	}
	
	
	@PreDestroy
	public void preDestroy() {
		LOG.info("浅梦gitHub:https://github.com/starmcc/QMBoootFrame");
	}
}
