package draft;

import org.junit.jupiter.api.Test;

import begyyal.web.WebResourceGetter;


public class draft {	
    @Test
    public void aaaa() {
	var url = "https://www.investing.com/indices/world-indices";
	var ho = WebResourceGetter.getHtmlObject(url);
	var l = ho.getElementById("pair_178").getElementsByClass("pid-178-last").getTip().getContents().getTip();
	System.out.println(ho.isSuccess());
	System.out.println(l);
    }
}
